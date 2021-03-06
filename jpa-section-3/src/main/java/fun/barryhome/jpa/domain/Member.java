package fun.barryhome.jpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created on 2020/3/18 0018 22:02
 *
 * @author Administrator
 * Description:
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
