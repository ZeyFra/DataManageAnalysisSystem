package zeyfra.dmas.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import zeyfra.dmas.core.api.ApiError;
import zeyfra.dmas.core.api.ApiRest;

/**
 * @author ZeyFra
 * @date 2021/2/16 13:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException{

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String msg;
    /**
     * 从结果初始化
     * @param apiRest
     */
    public ServiceException(ApiRest apiRest){
        this.code = apiRest.getCode();
        this.msg = apiRest.getMessage();
    }
    /**
     * 从枚举中获取参数
     * @param apiError
     */
    public ServiceException(ApiError apiError){
        this.code = apiError.getCode();
        this.msg = apiError.msg;
    }

}
