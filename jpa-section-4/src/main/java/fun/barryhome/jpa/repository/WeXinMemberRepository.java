package fun.barryhome.jpa.repository;

import fun.barryhome.jpa.domain.WeXinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 2020/2/28 0028 17:21
 *
 * @author Administrator
 * Description:
 */

@Repository
public interface WeXinMemberRepository extends JpaRepository<WeXinMember, String> {

    WeXinMember findFirstByMemberName(String memberName);

    WeXinMember findFirstByNickName(String nickName);

    WeXinMember findFirstByMemberCode(String memberCode);
}
