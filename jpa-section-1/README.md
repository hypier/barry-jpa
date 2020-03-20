# JPA对象关系映射--值对象映射

在领域驱动设计中，有一些结构主表和明细表必须一起显现才能表达业务意义，明细表不能单独使用被称为值对象，主表表达业务对象称为实体，也称为聚合根。

## 1. 在实体中的值对象上增加`@OneToMany`注解

```java
@Data
@Entity
public class SaleOrder {

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
     * 订单明细
     */
    @OneToMany
    private List<OrderDetail> orderDetailList;
}


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

```

*运行后从数据库上看，形成3个表
 - sale_order 主表
 - order_detail 明细表
 - sale_order_order_detail_list 关系表
 
## 2. 增加`@JoinColumn`注解，去掉关系表

```java
@OneToMany
@JoinColumn
private List<OrderDetail> orderDetailList;
```

明细表结构：
>Table: **order_detail**
Columns:
order_detail_id int(11) AI PK 
discount decimal(19,2) 
product_code varchar(255) 
quantity int(11) 
sale_price decimal(19,2) 
order_detail_list_order_id int(11)

从结构上讲自动增加了一个**order_detail_list_order_id int(11)** 字段，并做了外键关联

## 3. 使用非主键做关联
在特殊场景下，不用主键id做，使用更有意义的order_code

```java
@OneToMany
@JoinColumn(name = "orderCode", referencedColumnName = "orderCode")
private List<OrderDetail> orderDetailList;
```

``name``为子表的关联属性

``referencedColumnName``为主表的关联属性

>**注意：** 使用此注解后，两个表没有自动建立外键依赖，且主表的order_code字段为建唯一索引

## 4. 执行写入操作时报错

> **问题1：** fun.barryhome.jpa.domain.SaleOrder cannot be cast to java.io.Serializable

**原因：** Hibernate有二级缓存，缓存会将对象写进硬盘，就必须序列化。 

**解决方法：**
```
public class SaleOrder implements Serializable {
    ...
}
```
---
> **问题2：** object references an unsaved transient instance - save the transient instance before flushing: fun.barryhome.jpa.domain.OrderDetail

**原因：** 当子对象不存在时，保存主对象时，必须级联保存子对象

**解决方法：** 
```java
@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
@JoinColumn(name = "saleOrderCode", referencedColumnName = "orderCode")
private List<OrderDetail> orderDetailList;
```

---

## 5. 相关代码
```java
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
     * 订单明细
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "saleOrderCode", referencedColumnName = "orderCode")
    private List<OrderDetail> orderDetailList;
}

@Entity
@Data
@Builder
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


```

![此处输入图片的描述][1]


  [1]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG