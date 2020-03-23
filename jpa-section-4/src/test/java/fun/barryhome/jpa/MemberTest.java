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

import java.util.List;
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
        storeMember.setMemberCard("S00001");
        storeMember.setMemberLevel(2);

        memberRepository.save(storeMember);
    }

    @Test
    public void weXinAdd() {
        WeXinMember weXinMember = new WeXinMember();
        weXinMember.setMemberCode("W001");
        weXinMember.setMemberName("微信会员");
        weXinMember.setNickName("twoDog");
        weXinMember.setOpenId(UUID.randomUUID().toString());

        memberRepository.save(weXinMember);
    }

    @Test
    public void add(){
        storeAdd();
        weXinAdd();
    }


    @Test
    public void query() {
        Member member = memberRepository.getOne("W001");

        if (Hibernate.unproxy(member) instanceof WeXinMember) {
            WeXinMember weXinMember = (WeXinMember) (Hibernate.unproxy(member));

            //memberRepository.delete(weXinMember);
            System.err.println(weXinMember);
        }

        Member member2 = memberRepository.getOne("S001");

        if (Hibernate.unproxy(member2) instanceof StoreMember) {
            StoreMember storeMember = (StoreMember) (Hibernate.unproxy(member2));

            System.err.println(storeMember);
        }

        WeXinMember member3 = memberRepository.findFirstByMemberCode("W001");

        System.err.println(member3);
    }

    @Test
    public void wxQuery() {
        WeXinMember weXinMember = memberRepository.findFirstByMemberCode("W001");

        System.err.println(weXinMember);
    }

    @Test
    public void wxQuery2() {
        WeXinMember weXinMember = weXinMemberRepository.findFirstByNickName("twoDog");

        System.err.println(weXinMember);
    }

    @Test
    public void storeQuery() {
        StoreMember storeMember = memberRepository.findTop1ByMemberCode("S001");

        System.err.println(storeMember);
    }

    @Test
    public void queryAll() {
        List<Member> list = memberRepository.findAll();

        System.err.println(list);
    }
}