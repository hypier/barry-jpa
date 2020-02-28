package fun.barryhome.jpa.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created on 2020/2/28 0028 17:11
 *
 * @author Administrator
 * Description:
 */
@Entity
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 售价
     */
    private BigDecimal salePrice;
    /**
     * 折扣
     */
    private BigDecimal discount;
}
