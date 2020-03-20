package fun.barryhome.jpa.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.DiscriminatorColumn;
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
@DiscriminatorColumn(name = "member_type")
@DiscriminatorValue("store")
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
