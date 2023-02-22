package net.whir.hos.labunion.moneyAction.controller;

import net.whir.hos.labunion.moneyAction.bean.WalletExpend;
import net.whir.hos.labunion.moneyAction.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;
    /**
     * 1.查询用户钱包余额
     * @param employeeId
     * @return
     */
    @RequestMapping("/remaind/{employeeId}")
    public BigDecimal employeeRemaind(@PathVariable Long employeeId){
        return walletService.employeeRemaind(employeeId);
    }

    /**
     * 2.用户消费100元的接口
     */
    @RequestMapping("/expend/{employeeId}/{walletId}/{cost}")
    public Boolean expend(@PathVariable Long employeeId,@PathVariable Long walletId ,@PathVariable BigDecimal cost){
        return walletService.expend(employeeId,walletId ,cost);
    }

    /**
     * 3.用户退款20元的接口
     */
    @RequestMapping("/refund/{employeeId}/{walletId}/{walletExpendId}")
    public Boolean refund(@PathVariable Long employeeId,@PathVariable Long walletId ,@PathVariable Long walletExpendId){
        return walletService.refund(employeeId,walletId,walletExpendId);
    }

    /**
     * 4.钱包金额变动明细接口
     */
    @RequestMapping("/detail/{employeeId}")
    public List<WalletExpend> costDetail(@PathVariable Long employeeId){
        return walletService.costDetail(employeeId);
    }
}
