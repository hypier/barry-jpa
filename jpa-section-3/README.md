# JPA对象型属性操作

领域驱动设计核心是领域对象识别，一切操作皆是对象，这也是面向对象编程所倡导的。在设计实体属性时，除了数据库能识别的标准数据类型外，也越来越多考虑复合型的对象属性。让我们的设计视角为数据存储的层面转向客观事物存在的实际表象。ORM框架也就是为此提供技术支撑，至少JPA朝此方向不断完善的。

**先上代码：***（此代码主要为展示功能而设计，不一定具体设计的合理性，由于篇幅代码略有删减）*

```java
@Entity
public class SaleOrder implements Serializable {

    @Column(unique = true, updatable = false)
    private String orderCode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "stationCode", column = @Column(name = "in_station_code")),
            @AttributeOverride(name = "storage.storageCode", column = @Column(name = "in_storage_code"))
    })
    private Station inStation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "stationCode", column = @Column(name = "out_station_code")),
            @AttributeOverride(name = "storage.storageCode", column = @Column(name = "out_storage_code"))
    })
    private Station outStation;
    
    private LocalDateTime saleTime;

    private Address address;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "memberCode", referencedColumnName = "memberCode")
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "saleOrderCode", referencedColumnName = "orderCode")
    private List<OrderDetail> orderDetailList;
}

```

## 1. 使用`@OneToMany`做关联
 **Member**为引用对象，使用memberCode做两个表的关联，SaleOrder不能有操作Member对象的权限，只做级联刷新，使用`CascadeType.REFRESH`。
在设置会员对象时，这里只要求有memberCode便足够
```java
saleOrder.setMember(Member.builder().memberCode("M001").build());
```

当然，在实际业务中，订单和会员两个模块应该是分离的，属于不同的微服务，这样可以直接使用**private String memberCode**即可

## 2. 使用`@Enumerated`保存枚举值
程序中为了让代码更友好，大部分使用枚举对象来表示实体的状态，而在数据库字段中表示状态值一般我们使用int，但可读性较差。在一些状态较多的情况下，为了增强数据的可读性，可以使用string来表示
```java
@Enumerated(EnumType.STRING)
private OrderState orderState;
```

## 3. 使用`@Embeddable`将子表字段合并到主表
**Address**对象是**SaleOrder**的一个属并一直是saleOrder的一部分，在数据库层面不单独维护表，直接将所有字段合并入Sale_order表中
``` java
@Embeddable
public class Address {
    private String userName;
    private String city;
    private String street;
}
```
> **有人会问：**为什么不将Address对象的属性直接放在SaleOrder中呢
> **答：**这样做在技术层面是没有问题的，但设计上address中的属性 userName,city,street放入主对象的话就破坏了saleOrder的结构完整性，没有了3个属性共同表示地址的准确语意表达，维护起来也较麻烦

## 4. 使用`@AttributeOverride`重写字段名
如果一个主对象引用两个同样的子对象属性时，为引发两个子对象属性字段重名，故可以使用`@AttributeOverride`重写属性的字段名，也可适用于子对象的子对象
```java
@Embedded
@AttributeOverrides({
        @AttributeOverride(name = "stationCode", column = @Column(name = "in_station_code")),
        @AttributeOverride(name = "storage.storageCode", column = @Column(name = "in_storage_code"))
})
private Station inStation;

...

@Embeddable
public class Station {
    @Column(updatable = false)
    private String stationCode;

    @Transient
    private String stationName;

    private Storage storage;
}

@Embeddable
public class Storage {
    private String storageCode;

    @Transient
    private String storageName;
}

```

## 5. 使用`@Convert`做类型转换
我们知道mysql的datetime类型是没有百分秒的，但我们又需要百分秒，数据库里可以以时间戳Long类型保存，为了增强可读性程序里使用LocalDateTime，我们就可以使用`@Convert`做类型转换，**autoApply=true**为自动转换，当在程序里定义的类型和想要存储在数据库的类型不一致的情况下都可以使用`@Convert`进行类型转换

```java

private LocalDateTime saleTime;
...

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Long> {
    @Override
    public Long convertToDatabaseColumn(LocalDateTime localDateTime) {
        if (localDateTime == null){
            return null;
        }

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long aLong) {
        if (null == aLong) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.systemDefault());
    }
}
```


## 6. 使用抽象类属性
```java
public class OrderDetail {
    ...
    @Column(name = "product_code")
    @Convert(converter = ProductConverter.class)
    private AbstractProduct abstractProduct;
}

public abstract class AbstractProduct {
    private String productCode;
}

public class Book extends AbstractProduct{
    private String bookIsbn;
    private String bookName;
    private String author;
    private boolean isMagazine(){
        return true;
    }
}

public class Food extends AbstractProduct {
    private String foodCode;
    private String foodName;
    private Integer expiryDate;
    private boolean isExpires(){
        return false;
    }
}

```

在属性对象不明确的情况下可以定义抽象类，使用`@Convert`做类型转换

```java
public class ProductConverter implements AttributeConverter<AbstractProduct, String> {

    @Override
    public String convertToDatabaseColumn(AbstractProduct attribute) {
        if (attribute instanceof Food) {
            return ((Food) attribute).getFoodCode();
        } else if (attribute instanceof Book) {
            return ((Book) attribute).getBookIsbn();
        }
        return "";
    }


    @Override
    public AbstractProduct convertToEntityAttribute(String dbData) {
        if (dbData.startsWith("B")) {
            return Book.builder().bookIsbn(dbData).build();
        } else {
            return Food.builder().foodCode(dbData).build();
        }
    }
}
```

使用抽象类定义属性，主要目的是保持属性类的多样性或扩展性，对于数据库本身可能是固定的一个值，但对于实体对象，程序设计来讲可以保持更多变化的可能性。

> **总结：** 本节主要讲述了实体属性对象设置关系，主要目的是维持对象的结构清晰和可扩展性，也提供了另一种设计的可能性。但在实现业务过程中还是要因地制宜，根据实际情况出发，避免过度设计而增加复杂度


 
## 7. 源代码
[https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-3][1]


![此处输入图片的描述][2]


  [1]: https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-3
  [2]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG


