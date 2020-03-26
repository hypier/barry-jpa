package fun.barryhome.jpa.domain.event;

import fun.barryhome.jpa.domain.SaleOrder;
import fun.barryhome.jpa.domain.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created on 2020/3/26 15:15
 *
 * @author heyong
 * Description:
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderEvent {

    private SaleOrder saleOrder;

    private State state;

    public SaleOrderEvent(SaleOrder saleOrder) {
        this.saleOrder = saleOrder;
        state = saleOrder.getState();
    }


}
