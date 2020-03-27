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
        System.err.println("dept-event1:" + departmentEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT,condition = "#departmentEvent.getState().toString() == 'SUCCEED'")
    public void departmentCreatedSucceed(DepartmentEvent departmentEvent){
        System.err.println("dept-event2:" + departmentEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK,condition = "#departmentEvent.getState().toString() == 'SUCCEED'")
    public void departmentCreatedFailed(DepartmentEvent departmentEvent){
        System.err.println("dept-event3:" + departmentEvent);
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, condition = "#saleOrderEvent.getState().toString() == 'SUCCEED'")
    public void saleOrderCreated(SaleOrderEvent saleOrderEvent){
        System.err.println("sale-event succeed:" + saleOrderEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void saleOrderCreatedFailed(SaleOrderEvent saleOrderEvent){
        System.out.println("sale-event failed:" + saleOrderEvent);
    }
}
