package fun.barryhome.jpa.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Created on 2020/3/18 0018 22:00
 *
 * @author Administrator
 * Description:
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Station {

    /**
     * 站点代码
     */
    @Column(updatable = false)
    private String stationCode;

    /**
     * 站点名称
     */
    @Transient
    private String stationName;

    /**
     * 库位号
     */
    private Storage storage;



}
