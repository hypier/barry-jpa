# 6. 基础Spring Data的领域事件发布

标签（空格分隔）： Jpa SpringData DDD

---
[TOC]

领域事件发布是一个领域对象为了让其它对象知道自己已经处理完成某个操作时发出的一个通知，事件发布力求从代码层面让自身对象与外部对象解耦，并减少技术代码入侵。

## 一、 手动发布事件

```java
// 实体定义
@Entity
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    @Enumerated(EnumType.STRING)
    private State state;
}

// 事件定义
public class DepartmentEvent {
    private Department department;
    private State state;
    public DepartmentEvent(Department department) {
        this.department = department;
        state = department.getState();
    }
}

// 领域服务
@Service
public class ApplicationService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional(rollbackFor = Exception.class)
    public void departmentAdd(Department department) {
        departmentRepository.save(department);
        // 事件发布
        applicationEventPublisher.publishEvent(new DepartmentEvent(department));
    }
}

```

使用`applicationEventPublisher.publishEvent`在领域服务处理完成后发布领域事件，此方法需要在业务代码中显式发布事件，并在领域服务里引入ApplicationEventPublisher类，但对领域服务本身有一定的入侵性，但灵活性较高。

## 二、 自动发布事件

```java
// 实体定义
@Entity
public class SaleOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;
   
    @Enumerated(EnumType.STRING)
    private State state;

    // 返回类型定义
    @DomainEvents
    public List<Object> domainEvents(){
        return Stream.of(new SaleOrderEvent(this)).collect(Collectors.toList());
    }

    // 事件发布后callback
    @AfterDomainEventPublication
    void callback() {
        System.err.println("ok");
    }
}

// 事件定义
public class SaleOrderEvent {
    private SaleOrder saleOrder;
    private State state;
    public SaleOrderEvent(SaleOrder saleOrder) {
        this.saleOrder = saleOrder;
        state = saleOrder.getState();
    }
}

// 领域服务
@Service
public class ApplicationService {
    @Autowired
    private OrderRepository orderRepository;
    
    @Transactional(rollbackFor = Exception.class)
    public void saleOrderAdd(SaleOrder saleOrder) {
        orderRepository.save(saleOrder);
    }
}

```

使用`@DomainEvents`定义事件返回的类型，必须是一个集合，使用`@AfterDomainEventPublication`定义事件发布后的回调。
此方法实事件类型定义在实体中，与领域服务完全解耦，没有入侵。系统会在**orderRepository.save(saleOrder)**后自动调用事件发布，另**delete**方法不会调用事件发布。

## 三、 事件监听

```java
@Component
public class ApplicationEventProcessor {

    @EventListener(condition = "#departmentEvent.getState().toString() == 'SUCCEED'")
    public void departmentCreated(DepartmentEvent departmentEvent) {
        System.err.println("dept-event1:" + departmentEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#saleOrderEvent.getState().toString() == 'SUCCEED'")
    public void saleOrderCreated(SaleOrderEvent saleOrderEvent) {
        System.err.println("sale-event succeed1:" + saleOrderEvent);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, condition = "#saleOrderEvent.getState().toString() == 'SUCCEED'")
    public void saleOrderCreatedBefore(SaleOrderEvent saleOrderEvent) {
        System.err.println("sale-event succeed2:" + saleOrderEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void saleOrderCreatedFailed(SaleOrderEvent saleOrderEvent) {
        System.out.println("sale-event failed:" + saleOrderEvent);
    }
}

```
### 1. 使用`@EventListener`监听事件
`@EventListener`没有事务支持，只要事件发出就可监控到

```java
@Transactional(rollbackFor = Exception.class)
public void departmentAdd(Department department) {
    departmentRepository.save(department);
    applicationEventPublisher.publishEvent(new DepartmentEvent(department));
    throw new RuntimeException("failed");
}
```
上述情况会造成事务失败回滚，但事件监控端已经执行，可能导致数据不一致的情况发生

### 2. 使用`@TransactionalEventListener`监听事件

- `TransactionPhase.BEFORE_COMMIT` 事务提交前
- `TransactionPhase.AFTER_COMMIT` 事务提交后
- `TransactionPhase.AFTER_ROLLBACK` 事务回滚后
- `TransactionPhase.AFTER_COMPLETION` 事务完成后

使用`TransactionPhase.AFTER_COMMIT`可在事务完成后，再执行事件监听方法，从而保证数据的一致性

### 3. `TransactionPhase.AFTER_ROLLBACK`回滚事务问题
```java
@Async
@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK, condition = "#departmentEvent.getState().toString() == 'SUCCEED'")
public void departmentCreatedFailed(DepartmentEvent departmentEvent) {
    System.err.println("dept-event3:" + departmentEvent);
}
```
由于`@DomainEvents`作用在实体上的，只有刚orderRepository.save(saleOrder)执行成功后才会发送事件，故**AFTER_ROLLBACK**方法只会在同一事务中其它语句执行失败或显式rollback时才会执行，如果save方法执行失败，将不会监听到回滚事件。

### 4. `@Async`异步事件监听
- 没有此注解事件监听方法与主方法为一个事务。
- 使用此注解将脱离原有事务，**BEFORE_COMMIT**也无法拦截事务提交前时刻
- 此注解需要配合`@EnableAsync`一起使用

## 四、 总结
通过对**@DomainEvents**、**@TransactionalEventListener**的使用，在有效的解决领域事件发布的情况下，减少了对业务代码的入侵，同时尽一步解决了数据一致性问题。
在分布式结构下，通过MQ发送事件通知给其它服务，为解决一致性问题，防止对方服务处理失败可先将事件保久化到数据库后，再重试。

## 五、 源码
[https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-5][1]


![此处输入图片的描述][2]


  [1]: https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-5
  [2]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG

