package fun.barryhome.jpa.domain;

import fun.barryhome.jpa.domain.enums.OrderState;
import fun.barryhome.jpa.domain.valueobject.Address;
import fun.barryhome.jpa.domain.valueobject.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
    /**
     * 订单号
     */
    @Column(unique = true, updatable = false)
    private String orderCode;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 销售时间
     */
    private LocalDateTime saleTime;

    /**
     * 调入站点
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "stationCode", column = @Column(name = "in_station_code")),
            @AttributeOverride(name = "storage.storageCode", column = @Column(name = "in_storage_code"))
    })
    private Station inStation;

    /**
     * 调出站点
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "stationCode", column = @Column(name = "out_station_code")),
            @AttributeOverride(name = "storage.storageCode", column = @Column(name = "out_storage_code"))
    })
    private Station outStation;

    /**
     * 地址
     */
    private Address address;

    /**
     * 会员
     */
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "memberCode", referencedColumnName = "memberCode")
    private Member member;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    /**
     * 订单明细
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "saleOrderCode", referencedColumnName = "orderCode")
    private List<OrderDetail> orderDetailList;
}
