package fun.barryhome.jpa.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created on 2020/3/12 0012 22:06
 *
 * @author Administrator
 * Description:
 */
@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    private String employeeCode;

    private String employeeName;
}
