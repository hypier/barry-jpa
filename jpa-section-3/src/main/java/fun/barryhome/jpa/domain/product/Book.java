package fun.barryhome.jpa.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2020/3/18 0018 22:57
 *
 * @author Administrator
 * Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book extends AbstractProduct{

    private String BookIsbn;

    private String BookName;
}
