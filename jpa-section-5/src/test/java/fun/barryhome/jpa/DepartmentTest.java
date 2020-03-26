package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.ApplicationService;
import fun.barryhome.jpa.domain.Department;
import fun.barryhome.jpa.domain.Employee;
import fun.barryhome.jpa.domain.OrderDetail;
import fun.barryhome.jpa.domain.SaleOrder;
import fun.barryhome.jpa.domain.enums.State;
import fun.barryhome.jpa.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created on 2020/3/12 0012 22:11
 *
 * @author Administrator
 * Description:
 */
@SpringBootTest
public class DepartmentTest {
    @Autowired
    private ApplicationService applicationService;

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
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(Employee.builder()
                .employeeCode("E002")
                .employeeName("员工2")
                .build());

        department.setEmployeeList(employeeList);
        department.setState(State.SUCCEED);

        applicationService.departmentAdd(department);
    }

    @Test
    public void saleAdd() {
        SaleOrder saleOrder = SaleOrder.builder()
                .orderCode(UUID.randomUUID().toString())
                .orderCode("6dc9f637-290a-4f11-a79b-bc1b33cccdbd")
                .state(State.SUCCEED)
                .tradeAmount(BigDecimal.ONE)
                .build();

        List<OrderDetail> list = new ArrayList<>();
        OrderDetail orderDetail = OrderDetail.builder()
                .discount(BigDecimal.ONE)
                .productCode("001")
                .salePrice(BigDecimal.ONE)
                .quantity(1)
                .build();

        list.add(orderDetail);

        saleOrder.setOrderDetailList(list);

        applicationService.saleOrderAdd(saleOrder);
    }

}
