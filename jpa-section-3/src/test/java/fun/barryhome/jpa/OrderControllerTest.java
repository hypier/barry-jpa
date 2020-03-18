package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.Member;
import fun.barryhome.jpa.domain.OrderDetail;
import fun.barryhome.jpa.domain.SaleOrder;
import fun.barryhome.jpa.domain.enums.OrderState;
import fun.barryhome.jpa.domain.valueobject.Address;
import fun.barryhome.jpa.domain.valueobject.Station;
import fun.barryhome.jpa.domain.valueobject.Storage;
import fun.barryhome.jpa.repository.MemberRepository;
import fun.barryhome.jpa.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created on 2020/3/1 0001 17:53
 *
 * @author Administrator
 * Description:
 */
@SpringBootTest
class OrderControllerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void member(){

        if (memberRepository.existsById("M001")){
            return;
        }

        Member member = Member.builder()
                .memberCode("M001")
                .memberName("会员001")
                .build();

        memberRepository.save(member);
    }

    @Test
    void add() {


        SaleOrder saleOrder = SaleOrder.builder()
                .orderCode(UUID.randomUUID().toString())
                .orderState(OrderState.SUCCEED)
                .tradeAmount(BigDecimal.ONE)
                .address(Address.builder()
                        .userName("顾客1")
                        .city("CQ")
                        .street("ST1")
                        .build())
                .member(Member.builder().memberCode("M001").build())
                .inStation(Station.builder()
                        .stationCode("S001")
                        .storage(Storage.builder().storageCode("SS001").build())
                        .build())
                .outStation(Station.builder()
                        .stationCode("S002")
                        .storage(Storage.builder().storageCode("SS002").build())
                        .build())
                .build();

        List<OrderDetail> list = new ArrayList<>();
        OrderDetail orderDetail = OrderDetail.builder()
                .discount(BigDecimal.ONE)
                .salePrice(BigDecimal.ONE)
                .quantity(1)
                .build();

        list.add(orderDetail);

        saleOrder.setOrderDetailList(list);

        orderRepository.save(saleOrder);
    }
}