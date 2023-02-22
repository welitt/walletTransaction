package net.whir.hos.labunion.moneyAction.service;

import lombok.extern.slf4j.Slf4j;
import net.whir.hos.labunion.moneyAction.bean.Wallet;
import net.whir.hos.labunion.moneyAction.bean.WalletExpend;
import net.whir.hos.labunion.moneyAction.dao.WalletDao;
import net.whir.hos.labunion.moneyAction.dao.WalletExpendDao;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class WalletService {
    @Autowired
    private WalletDao walletDao;
    @Autowired
    private WalletExpendDao walletExpendDao;
    @Autowired
    private RedissonClient redissonClient;
    public BigDecimal employeeRemaind(Long employeeId) {
        Wallet wallet =  walletDao.findByEmployeeId(employeeId);
        return wallet == null ? BigDecimal.valueOf(0.0) : wallet.getMoney();
    }

    public Boolean expend(Long employeeId,Long walletId , BigDecimal cost) {
        String prefix = "wallet:";
        RLock redissonLock = redissonClient.getLock(prefix + walletId);
        //加锁
        redissonLock.lock();
        log.info("employeeId:{},花费{},获得锁",employeeId,cost);
        Boolean flag = false;
        try{
            flag = ((WalletService)AopContext.currentProxy()).expendDetail(employeeId,walletId,cost);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            redissonLock.unlock();
        }
        return flag;
    }

    /**
     * 花费的业务代码
     * @param employeeId
     * @param walletId
     * @param cost
     * @return
     */
    @Transactional
    public Boolean expendDetail(Long employeeId, Long walletId, BigDecimal cost) {
        Wallet wallet = walletDao.findById(walletId).orElse(null);
        if(wallet == null || wallet.getMoney().compareTo(cost) < 0)
            return false;
        BigDecimal money = wallet.getMoney();
        BigDecimal remain = money.subtract(cost);
        WalletExpend walletExpend = WalletExpend.builder().employeeId(employeeId)
                .wallet(wallet).preMoney(money).afterMoney(remain).cost(cost).build();
        wallet.setMoney(remain);
        walletExpendDao.save(walletExpend);
        walletDao.save(wallet);
        return true;
    }

    public Boolean refund(Long employeeId, Long walletId, Long walletExpendId) {
        String prefix = "wallet:";
        RLock redissonLock = redissonClient.getLock(prefix + walletId);
        //加锁
        redissonLock.lock();
        log.info("employeeId:{},退款{},获得锁",employeeId);
        Boolean flag = false;
        try{
            flag = ((WalletService)AopContext.currentProxy()).refundDetail(employeeId,walletId,walletExpendId);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            redissonLock.unlock();
        }
        return flag;
    }

    @Transactional
    public Boolean refundDetail(Long employeeId, Long walletId, Long walletExpendId) {
        WalletExpend walletExpend = walletExpendDao.findById(walletExpendId).orElse(null);
        if(walletExpend == null)
            return false;
        //已经退过款了
        if(walletExpend.getRefund() != null && walletExpend.getRefund())
        {
            log.error("employeeId:{} , walletExpendId:{} , 此前已经退款成功 , 此次退款失败",employeeId,walletExpendId);
            return false;
        }
        //表示退款
        walletExpend.setRefund(true);
        Wallet wallet = walletDao.findById(walletId).orElse(null);
        if(wallet == null)
            return false;
        wallet.setMoney(wallet.getMoney().add(walletExpend.getCost()));
        walletExpendDao.save(walletExpend);
        walletDao.save(wallet);
        return true;
    }

    public List<WalletExpend> costDetail(Long employeeId) {
       return walletExpendDao.findByEmployeeId(employeeId);
    }
}
