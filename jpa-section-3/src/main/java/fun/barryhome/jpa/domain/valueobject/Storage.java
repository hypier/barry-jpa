package fun.barryhome.jpa.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Created on 2020/3/18 0018 22:08
 *
 * @author Administrator
 * Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class Storage {
    /**
     * 库房代码
     */
    private String storageCode;

    /**
     * 库房名称
     */
    @Transient
    private String storageName;
}
