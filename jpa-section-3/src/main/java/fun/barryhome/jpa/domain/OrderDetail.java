package fun.barryhome.jpa.domain;

import fun.barryhome.jpa.domain.converter.ProductConverter;
import fun.barryhome.jpa.domain.product.AbstractProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * Created on 2020/2/28 0028 17:11
 *
 * @author Administrator
 * Description:
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;

    /**
     * 产品
     */
    @Column(name = "product_code")
//    @Convert(converter = ProductConverter.class)
    private AbstractProduct abstractProduct;

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
