package fun.barryhome.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 2020/3/12 0012 22:04
 *
 * @author Administrator
 * Description:
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    private String departmentCode;

    private String departmentName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "departmentCode", referencedColumnName = "departmentCode")
    private List<Employee> employeeList;

    @DomainEvents
    public List<Object> domainEvents() {
        List<Object> events= new ArrayList<>();
        events.add(this);
        return events;
    }

    @AfterDomainEventPublication
    public void callbackMethod() {
        System.out.println("callbackMethod什么时候调用呢");
    }
}










