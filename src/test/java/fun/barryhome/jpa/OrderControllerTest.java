package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.OrderDetail;
import fun.barryhome.jpa.domain.SaleOrder;
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

    @Test
    void add() {
        SaleOrder saleOrder = SaleOrder.builder()
                .orderCode(UUID.randomUUID().toString())
                .orderState("SUCCEED")
                .tradeAmount(BigDecimal.ONE)
                .build();

        List<OrderDetail> list = new ArrayList<>();
        OrderDetail orderDetail = OrderDetail.builder()
                .discount(BigDecimal.ONE)
                .productCode("001")
                .salePrice(BigDecimal.ONE)
                .quantity(1)
                .build();

        list.add(orderDetail);

        saleOrder.setOrderDetailList(list);

        orderRepository.save(saleOrder);
    }
}