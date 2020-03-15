package fun.barryhome.jpa.domain;

import lombok.Builder;
import lombok.Data;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
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
@Builder
public class SaleOrder implements Serializable {

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
     * 地址
     */
    @ManyToOne
    private Address address;
    /**
     * 订单明细
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "saleOrderCode", referencedColumnName = "orderCode")
    private List<OrderDetail> orderDetailList;
}
