package fun.barryhome.jpa.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created on 2020/3/20 14:02
 *
 * @author heyong
 * Description:
 */
@Data
@Entity
@ToString(callSuper = true)
@DiscriminatorValue("store")
@DiscriminatorOptions
public class StoreMember extends Member {

    private String memberCard;

}
