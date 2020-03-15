package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.Department;
import fun.barryhome.jpa.domain.Employee;
import fun.barryhome.jpa.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020/3/12 0012 22:11
 *
 * @author Administrator
 * Description:
 */
@SpringBootTest
public class DepartmentTest {
    @Autowired
    private DepartmentRepository departmentRepository;

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

        department.setEmployeeList(employeeList);

        departmentRepository.save(department);
    }

    @Test
    public void query(){
        Department one = departmentRepository.getOne(2);
        System.err.println(one);
    }
}
