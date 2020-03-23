package fun.barryhome.jpa.repository;

import fun.barryhome.jpa.domain.Member;
import fun.barryhome.jpa.domain.StoreMember;
import fun.barryhome.jpa.domain.WeXinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 2020/2/28 0028 17:21
 *
 * @author Administrator
 * Description:
 */

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    WeXinMember findFirstByNickName(String nickName);

    WeXinMember findFirstByMemberCode(String memberCode);

    StoreMember findTop1ByMemberCode(String memberCode);

    Member findFirstByMemberName(String memberName);

}
