package fun.barryhome.jpa.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created on 2020/2/28 0028 17:18
 *
 * @author Administrator
 * Description:
 */
@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;
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
