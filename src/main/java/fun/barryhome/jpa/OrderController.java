package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.Order;
import fun.barryhome.jpa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created on 2020/2/28 0028 18:16
 *
 * @author Administrator
 * Description:
 */
@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping(path = "/query")
    public List<Order> query(){
        return orderRepository.findAll();
    }

    @PostMapping(path = "add")
    public Order add(@RequestBody Order order){
        return orderRepository.save(order);
    }
}
