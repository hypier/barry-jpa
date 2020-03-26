package fun.barryhome.jpa.appliaction;

import fun.barryhome.jpa.domain.event.DepartmentEvent;
import fun.barryhome.jpa.domain.event.SaleOrderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created on 2020/3/26 15:25
 *
 * @author heyong
 * Description:
 */
@Component
public class ApplicationEventProcessor {

    @Async
    @EventListener(condition = "#departmentEvent.getState().toString() == 'SUCCEED'")
    public void departmentCreated(DepartmentEvent departmentEvent){
        throw new RuntimeException("failed");
//        System.err.println("dept-event:" + departmentEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#saleOrderEvent.getState().toString() == 'SUCCEED'")
    public void saleOrderCreated(SaleOrderEvent saleOrderEvent){
//        throw new RuntimeException("succeed ");
        System.err.println("sale-event succeed:" + saleOrderEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK, classes = SaleOrderEvent.class)
    public void saleOrderCreatedFailed(SaleOrderEvent saleOrderEvent){
//        throw new RuntimeException("failed");
        System.out.println("sale-event failed:" + saleOrderEvent);
    }
}
