package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.Member;
import fun.barryhome.jpa.domain.StoreMember;
import fun.barryhome.jpa.domain.WeXinMember;
import fun.barryhome.jpa.repository.MemberRepository;
import fun.barryhome.jpa.repository.WeXinMemberRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


/**
 * Created on 2020/3/1 0001 17:53
 *
 * @author Administrator
 * Description:
 */
@SpringBootTest
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WeXinMemberRepository weXinMemberRepository;

    @Test
    public void storeAdd() {
        StoreMember storeMember = new StoreMember();
        storeMember.setMemberCode("S001");
        storeMember.setMemberName("线下会员");
        storeMember.setMemberType("store");
        storeMember.setMemberCard("S00001");
        storeMember.setMemberLevel(2);

        memberRepository.save(storeMember);
    }

    @Test
    public void weXinAdd() {
        WeXinMember weXinMember = new WeXinMember();
        weXinMember.setMemberCode("W001");
        weXinMember.setMemberName("微信会员");
        weXinMember.setMemberType("wexin");
        weXinMember.setNickName("twoDog");
        weXinMember.setOpenId(UUID.randomUUID().toString());

        memberRepository.save(weXinMember);
    }


    @Test
    public void query() {
        Member member = memberRepository.getOne("W001");

        if (Hibernate.unproxy(member) instanceof WeXinMember) {
            WeXinMember weXinMember = (WeXinMember) (Hibernate.unproxy(member));

            System.err.println(weXinMember);
        }

        Member member2 = memberRepository.getOne("S001");

        if (Hibernate.unproxy(member2) instanceof StoreMember) {
            StoreMember storeMember = (StoreMember) (Hibernate.unproxy(member2));

            System.err.println(storeMember);
        }
    }

    @Test
    public void wxQuery() {
        WeXinMember weXinMember = weXinMemberRepository.getOne("W001");

        System.err.println(weXinMember);
    }
}