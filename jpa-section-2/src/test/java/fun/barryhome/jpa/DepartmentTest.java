package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.Department;
import fun.barryhome.jpa.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        departmentRepository.save(department);
    }
}
