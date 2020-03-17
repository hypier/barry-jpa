package fun.barryhome.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import fun.barryhome.jpa.domain.Department;
import fun.barryhome.jpa.domain.Employee;
import fun.barryhome.jpa.domain.QDepartment;
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
//                .department(department)
                .build();
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(Employee.builder()
                .employeeCode("E002")
                .employeeName("员工2")
//                .department(department)
                .build());

        department.setEmployeeList(employeeList);

        departmentRepository.save(department);
    }

    @Test
    public void query(){
        Department one = departmentRepository.getOne(1);
//        one.getEmployeeList().remove(0);
        departmentRepository.delete(one);
        //System.err.println(one);
    }

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    public void queryForDsl(){
        QDepartment qDepartment = QDepartment.department;

        Department d001 = jpaQueryFactory.select(qDepartment).from(qDepartment)
                .where(qDepartment.departmentCode.eq("D001")).fetchOne();

        System.err.println(d001);
    }
}
