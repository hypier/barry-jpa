# 5. JPA对象继承关系

标签（空格分隔）： Jpa

---
[TOC]

在实体建模过程中，有些实体会有多种变形，其中大部分的属性都是共用的，只有一小部分是特有的。这时较优雅的设计是将共用的属性抽象出来形成基类，实现类再去扩展特有属性。领域服务可将通用服务抽象出来形成基类服务，再扩展特有服务。而Repository设计，一般情况也是先抽象基础，再扩展特有方法，调用时一般提供泛型支持，根据实现类的类型调用具体的Repository。
今天介绍使用`@Inheritance`注解让一个Repository支持所有实现类，从而简化Repository的设计。

## 一、对象建模

```java
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "member_type")
public class Member {
    @Id
    private String memberCode;
    private String memberName;
}


@DiscriminatorValue("store")
public class StoreMember extends Member {
    private String memberCard;
    private Integer memberLevel;
}

@DiscriminatorValue("wexin")
public class WeXinMember extends Member{
    private String openId;
    private String nickName;
}
```

 - `@Inheritance`用来配置父类
     - `InheritanceType.SINGLE_TABLE` 将所有实现类的所有字段映射到一个表里。
     - `InheritanceType.TABLE_PER_CLASS` 将每个实现类合并基类的字段映射到单独的表里，每个表相关独立且没有关联。
     - `InheritanceType.JOINED` 将基类和每个实现类分别映射到独立的表里，并使用主键进行关联，实现类只包含自己独有的字段。
 - `@DiscriminatorColumn(name = "member_type")` 用来做实现类区别标识的字段，如果不指定name，则会自动新建dtype字段。此字段系统会自动赋值，不需要人为指定，且不能作为属性存在。
 - `@DiscriminatorValue("wexin")` 实现类区别的标识值，jpa会根据具体标识值将数据持久化到对应的表中，查询语句也可自动识别类型

## 二、Repository设计

### 1. 查询
``` java
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    WeXinMember findFirstByNickName(String openId);
    WeXinMember findFirstByMemberCode(String memberCode);
    StoreMember findTop1ByMemberCode(String memberCode);
    Member findFirstByMemberName(String memberName);
}

```

 - 从上面可以看到，所有的实现类都可视为一个整体，可直接使用某个子类的属性做查询条件，可以有子类的返回值，也可以设置基类返回值。
 - 如果返回值为基类**Member**，jpa则返回Hibernate的代理类，需要`Hibernate.unproxy(member)`才能得到具体的实现类

```java
Member member = memberRepository.findFirstByMemberName("微信会员");
if (Hibernate.unproxy(member) instanceof WeXinMember) {
    WeXinMember weXinMember = (WeXinMember) (Hibernate.unproxy(member));
    System.err.println(weXinMember);
}
```

 - 如果返回值为实现类则可以直接使用
``` java
WeXinMember weXinMember = memberRepository.findFirstByMemberCode("W001");
```

### 2. 写入和删除
写入和删除操作，jpa都视为一个整体，可以直接使用memberRepository默认的方法

```java
WeXinMember weXinMember = new WeXinMember();
weXinMember.setMemberCode("W001");
weXinMember.setMemberName("微信会员");
weXinMember.setMemberType("wexin");
weXinMember.setNickName("twoDog");
weXinMember.setOpenId(UUID.randomUUID().toString());

memberRepository.save(weXinMember);

...
memberRepository.save(weXinMember);
...
memberRepository.delete(weXinMember);
```

## 三、Repository查询语句分析
使用哪种方式构建，主要考虑数据库的表结构关系

### 1. `InheritanceType.SINGLE_TABLE` 构建单表模式
不同的返回值类型，SQL语句有差别

**基类：**将所有字段都查询出来，有不必要的性能开销

```
Member findFirstByMemberCode(String memberCode);
```
```sql
 SELECT 
    member0_.member_code AS member_c2_0_0_,
    member0_.member_name AS member_n3_0_0_,
    member0_.member_card AS member_c4_0_0_,
    member0_.member_level AS member_l5_0_0_,
    member0_.nick_name AS nick_nam6_0_0_,
    member0_.open_id AS open_id7_0_0_,
    member0_.member_type AS member_t1_0_0_
FROM
    member member0_
WHERE
    member0_.member_code = 'W001'
```

**实现类：**只查询实现类的字段
``` java
WeXinMember findFirstByMemberCode(String memberCode);
```
``` mysql
SELECT 
    wexinmembe0_.member_code AS member_c2_0_,
    wexinmembe0_.member_name AS member_n3_0_,
    wexinmembe0_.nick_name AS nick_nam6_0_,
    wexinmembe0_.open_id AS open_id7_0_
FROM
    member wexinmembe0_
WHERE
    wexinmembe0_.member_type = 'wexin'
        AND wexinmembe0_.member_code = 'W001'

```

### 2. `InheritanceType.TABLE_PER_CLASS`构建独立表模式

**基类：**将所有子表都**union**进来再查询，此方法不可取，性能开销大
```
Member findFirstByMemberCode(String memberCode);
```
```sql
SELECT 
    member0_.member_code AS member_c1_0_0_,
    member0_.member_name AS member_n2_0_0_,
    member0_.member_card AS member_c1_1_0_,
    member0_.member_level AS member_l2_1_0_,
    member0_.nick_name AS nick_nam1_2_0_,
    member0_.open_id AS open_id2_2_0_,
    member0_.clazz_ AS clazz_0_
FROM
    (SELECT 
        member_code,
            member_name,
            NULL AS member_card,
            NULL AS member_level,
            NULL AS nick_name,
            NULL AS open_id,
            0 AS clazz_
    FROM
        member UNION ALL SELECT 
        member_code,
            member_name,
            member_card,
            member_level,
            NULL AS nick_name,
            NULL AS open_id,
            1 AS clazz_
    FROM
        store_member UNION ALL SELECT 
        member_code,
            member_name,
            NULL AS member_card,
            NULL AS member_level,
            nick_name,
            open_id,
            2 AS clazz_
    FROM
        we_xin_member) member0_
WHERE
    member0_.member_code = 'W001'
```

**实现类：**只查询实现类的字段
``` java
WeXinMember findFirstByMemberCode(String memberCode);
```
``` mysql
SELECT 
    wexinmembe0_.member_code AS member_c1_0_,
    wexinmembe0_.member_name AS member_n2_0_,
    wexinmembe0_.nick_name AS nick_nam1_2_,
    wexinmembe0_.open_id AS open_id2_2_
FROM
    we_xin_member wexinmembe0_
WHERE
    wexinmembe0_.member_code = 'W001'

```

### 3. `InheritanceType.JOINED`构建关联表模式

**基类：**将所有子表进行关联后再查询，子表多了性能开销大
```
Member findFirstByMemberCode(String memberCode);
```
```sql
SELECT 
    member0_.member_code AS member_c2_0_0_,
    member0_.member_name AS member_n3_0_0_,
    member0_1_.member_card AS member_c1_1_0_,
    member0_1_.member_level AS member_l2_1_0_,
    member0_2_.nick_name AS nick_nam1_2_0_,
    member0_2_.open_id AS open_id2_2_0_,
    member0_.member_type AS member_t1_0_0_
FROM
    member member0_
        LEFT OUTER JOIN
    store_member member0_1_ ON member0_.member_code = member0_1_.member_code
        LEFT OUTER JOIN
    we_xin_member member0_2_ ON member0_.member_code = member0_2_.member_code
WHERE
    member0_.member_code = 'W001'
```

**实现类：**关联主表和当前类型子表查询
``` java
WeXinMember findFirstByMemberCode(String memberCode);
```
``` mysql
SELECT 
    wexinmembe0_.member_code AS member_c2_0_,
    wexinmembe0_1_.member_name AS member_n3_0_,
    wexinmembe0_.nick_name AS nick_nam1_2_,
    wexinmembe0_.open_id AS open_id2_2_
FROM
    we_xin_member wexinmembe0_
        INNER JOIN
    member wexinmembe0_1_ ON wexinmembe0_.member_code = wexinmembe0_1_.member_code
WHERE
    wexinmembe0_.member_code = 'W001'

```

综上：如果有明确的类型时，查询方法的返回值应该设置为具体现实类，以便于优化查询语句

## 四、适用场景
此功能还是挺新奇的，适用于包含多种变形类操作的场景，此方法比直接使用泛型处理更方便，更容易处理数据，但需要关注数据库结构与查询语句的性能影响，建议使用**InheritanceType.JOINED**模式

## 五. 源代码
[https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-4][1]


![此处输入图片的描述][2]


  [1]: https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-4
  [2]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG
