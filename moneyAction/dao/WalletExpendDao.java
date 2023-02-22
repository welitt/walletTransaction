package net.whir.hos.labunion.moneyAction.dao;

import net.whir.hos.labunion.moneyAction.bean.WalletExpend;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletExpendDao extends JpaRepository<WalletExpend, Long> {

    List<WalletExpend> findByEmployeeId(Long employeeId);
}
