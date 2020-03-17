package fun.barryhome.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 2020/3/12 0012 22:06
 *
 * @author Administrator
 * Description:
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    private String employeeCode;

    private String employeeName;

    @ManyToOne
    @JoinColumn(name = "departmentCode", referencedColumnName = "departmentCode")
    private Department department;

    @DomainEvents
    public List<Object> domainEvents() {
        System.out.println("domainEvents什么时候调用呢");
        System.out.println(this);
        return Stream.of(this).collect(Collectors.toList());
    }

    @AfterDomainEventPublication
    public void callbackMethod() {
        System.out.println("callbackMethod什么时候调用呢");
    }
}
