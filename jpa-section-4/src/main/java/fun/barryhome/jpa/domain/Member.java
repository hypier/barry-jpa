package fun.barryhome.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import static javax.persistence.DiscriminatorType.STRING;

/**
 * Created on 2020/3/20 13:58
 *
 * @author heyong
 * Description:
 */
@Data
@Builder
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name = "member_type", discriminatorType=STRING, length=20)
@Inheritance(strategy = InheritanceType.JOINED)
public class Member {

    /**
     * 会员号
     */
    @Id
    private String memberCode;

    /**
     * 会员名称
     */
    private String memberName;
}
