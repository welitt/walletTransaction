package net.whir.hos.labunion.moneyAction.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.whir.hos.labunion.organization.bean.SimpleEmployee;
import net.whir.hos.labunion.welfare.bean.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;



/**
 * 钱包的钱钱
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet extends BaseBusEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "money")
    private BigDecimal money;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<WalletExpend> walletExpendList;

}

