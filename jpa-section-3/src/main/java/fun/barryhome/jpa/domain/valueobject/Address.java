package fun.barryhome.jpa.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Created on 2020/2/28 0028 17:18
 *
 * @author Administrator
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    /**
     * 姓名
     */
    private String userName;
    /**
     * 城市
     */
    private String city;
    /**
     * 街道
     */
    private String street;
}
