package fun.barryhome.jpa.repository;

import fun.barryhome.jpa.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 2020/3/12 0012 22:09
 *
 * @author Administrator
 * Description:
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
