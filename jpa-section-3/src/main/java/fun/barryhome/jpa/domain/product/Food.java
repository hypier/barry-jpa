package fun.barryhome.jpa.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2020/3/18 0018 22:56
 *
 * @author Administrator
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food extends AbstractProduct {

    private String foodCode;

    private String foodName;
}
