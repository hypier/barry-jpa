package fun.barryhome.jpa;

import fun.barryhome.jpa.domain.SaleOrder;
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
    public List<SaleOrder> query(){
        return orderRepository.findAll();
    }

    @PostMapping(path = "add")
    public SaleOrder add(@RequestBody SaleOrder saleOrder){
        return orderRepository.save(saleOrder);
    }
}
