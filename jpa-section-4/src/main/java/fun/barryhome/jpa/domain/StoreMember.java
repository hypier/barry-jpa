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
@DiscriminatorOptions(force = true)
public class StoreMember extends Member {

    /**
     * 会员卡号
     */
    private String memberCard;
    /**
     * 会员等级
     */
    private Integer memberLevel;
}
