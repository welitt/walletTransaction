package net.whir.hos.labunion.moneyAction.dao;

import net.whir.hos.labunion.moneyAction.bean.Wallet;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletDao extends JpaRepository<Wallet, Long> {
    Wallet findByEmployeeId(Long employeeId);
}
