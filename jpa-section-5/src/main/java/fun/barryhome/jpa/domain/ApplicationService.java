package fun.barryhome.jpa.domain;

import fun.barryhome.jpa.domain.Department;
import fun.barryhome.jpa.domain.SaleOrder;
import fun.barryhome.jpa.domain.event.DepartmentEvent;
import fun.barryhome.jpa.repository.DepartmentRepository;
import fun.barryhome.jpa.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created on 2020/3/26 15:18
 *
 * @author heyong
 * Description:
 */
@Service
public class ApplicationService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Transactional(rollbackFor = Exception.class)
    public void departmentAdd(Department department){
        departmentRepository.save(department);
        applicationEventPublisher.publishEvent(new DepartmentEvent(department));
    }

    @Transactional(rollbackFor = Exception.class)
    public void saleOrderAdd(SaleOrder saleOrder){
        orderRepository.save(saleOrder);
    }
}
