package fun.barryhome.jpa.domain.event;

import fun.barryhome.jpa.domain.Department;
import fun.barryhome.jpa.domain.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created on 2020/3/26 15:06
 *
 * @author heyong
 * Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEvent {

    private Department department;

    private State state;

    public DepartmentEvent(Department department) {
        this.department = department;
        state = department.getState();
    }

}
