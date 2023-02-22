package net.whir.hos.labunion.moneyAction.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.whir.hos.labunion.welfare.bean.Welfare;
import net.whir.hos.labunion.welfare.bean.WelfareScope;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 花费明细
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallet_expend")
public class WalletExpend extends BaseBusEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", updatable = false)
    private Wallet wallet;

    @Column(name = "pre_money")
    private BigDecimal preMoney;

    @Column(name = "after_money")
    private BigDecimal afterMoney;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "refund")
    private Boolean refund ;

    @Column(name = "info")
    private String info ;
}

