# JPA对象关系映射 -- 级联操作

对象映射关系分为单向关系和双向关系，单向关系只在一方对象上存在对方对象，双向关系是在双方对象上存在彼此对象。

## 一、单向关系

```java
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    private String departmentCode;
    
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn
    private List<Employee> employeeList;
}
```

在没有`@JoinColumn`时，将多增加一个中间关系表，由此表来维护两个对象关系，增加之后只有两个表，由**Employee**表维护关系。默认情况下jpa会使用主键来做关联，并在子表中增加外键约束。

## 二、单向关系使用code关联

在设计表结构时，主键一般会使用自增ID，但在做子表关联时由于分布式结构原因不想使用自增ID来做关系维护，则可自定义字段**code**来维护关系，如下：

```java
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    private String departmentCode;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "departmentCode", referencedColumnName = "departmentCode")
    private List<Employee> employeeList;
}
```

在employee表中会增加一个**department_code**字段来维护关联关系。
**注意**：从add操作中的SQL可以看出，employee是先insert之后，再去update关系字段的，多一步update。

```sql
Hibernate: insert into department (department_code, department_name) values (?, ?)
Hibernate: insert into employee (employee_code, employee_name) values (?, ?)
Hibernate: insert into employee (employee_code, employee_name) values (?, ?)
Hibernate: update employee set department_code=? where employee_id=?
Hibernate: update employee set department_code=? where employee_id=?
```

## 三、双向关系

```java
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    private String departmentCode;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "department")
    private List<Employee> employeeList;
}

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    private String employeeCode;

    @ManyToOne
    private Department department;
}

```

使用`mappedBy`将去掉中间关系表，由employee维护department的关系。

**注意事项**：
1.在add操作中子对象employee必须设置主对象，否则数据库层面是没有维护外键关系的，如：

``` java
@Test
public void add(){
    Department department = Department.builder()
            .departmentCode("D001")
            .departmentName("部门1")
            .build();

    Employee employee = Employee.builder()
            .employeeCode("E001")
            .employeeName("员工1")
            .build();
    // 如无此操作，将无关联关系        
    employee.setDepartment(department);
    
    List<Employee> employeeList = new ArrayList<>();
    employeeList.add(employee);

    department.setEmployeeList(employeeList);
    departmentRepository.save(department);
}
```

2.此种设置是不需要额外update关系的
``` sql
Hibernate: insert into department (department_code, department_name) values (?, ?)
Hibernate: insert into employee (department_department_id, employee_code, employee_name) values (?, ?, ?)
Hibernate: insert into employee (department_department_id, employee_code, employee_name) values (?, ?, ?)
```

3.关闭子对象中的父对象toString，避免无限循环调用

## 四、双向关系使用code关联
同单向关系类似，且有额外的update操作
code关联，使用JoinColumn时不可同时使用mappedBy

## 五、级联操作设置

 - CascadeType.PERSIST：级联保存，在保存department的同时保存employee对象
 - CascadeType.MERGE：级联更新，将department和employee视为一个整体，任何一个对象有变化，都会更新
 - CascadeType.REMOVE：级联删除
    - 当没有设置时，delete主对象时，子对象只是去掉关系；remove子对象时也只是去掉关系，如果增加`orphanRemoval = true`则会删除remove的子对象
    - 当有设置时，delete主对象时，子对象同样会被删除
 - CascadeType.REFRESH：级联刷新（较少使用），在并发的场景下避免脏数据
 - CascadeType.DETACH：级联脱管（较少使用）
 - CascadeType.ALL：以上全部，需要根据实际情况谨慎设置，以免产生混乱
 
## 六、源代码
[https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-2][1]


![此处输入图片的描述][2]


  [1]: https://gitee.com/hypier/barry-jpa/tree/master/jpa-section-2
  [2]: https://oscimg.oschina.net/oscnet/up-8969dabd3beeba071b59e61139a2bb8b22f.JPEG