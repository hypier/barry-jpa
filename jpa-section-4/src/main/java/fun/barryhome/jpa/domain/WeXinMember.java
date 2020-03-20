package fun.barryhome.jpa.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created on 2020/3/20 14:09
 *
 * @author heyong
 * Description:
 */
@Data
@Entity
@ToString(callSuper = true)
@DiscriminatorColumn(name = "member_type")
@DiscriminatorValue("wexin")
public class WeXinMember extends Member{

    /**
     * openId
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickName;
}
