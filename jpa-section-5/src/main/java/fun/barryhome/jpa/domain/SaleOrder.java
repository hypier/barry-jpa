package fun.barryhome.jpa.domain;

import fun.barryhome.jpa.domain.enums.State;
import fun.barryhome.jpa.domain.event.SaleOrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
    @Enumerated(EnumType.STRING)
    private State state;

    /**
     * 订单明细
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "saleOrderCode", referencedColumnName = "orderCode")
    private List<OrderDetail> orderDetailList;

    @DomainEvents
    public List<Object> domainEvents(){
        return Collections.singletonList(new SaleOrderEvent(this));
    }

    @AfterDomainEventPublication
    void callback() {
        System.err.println("ok");
    }
}
