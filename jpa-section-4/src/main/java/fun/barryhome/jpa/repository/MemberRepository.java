package fun.barryhome.jpa.repository;

import fun.barryhome.jpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on 2020/2/28 0028 17:21
 *
 * @author Administrator
 * Description:
 */

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

}
