package fun.barryhome.jpa.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created on 2020/2/28 0028 17:09
 *
 * @author Administrator
 * Description:
 */
@Data
@Entity
@Table(name = "sale_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 销售时间
     */
    private Date saleTime;
    /**
     * 订单状态
     */
    private String orderState;
    /**
     * 订单地址
     */
//    @Transient
    @ManyToOne
    private Address address;
    /**
     * 订单明细
     */
//    @Transient
    @OneToMany
    private List<OrderDetail> orderDetailList;
}
