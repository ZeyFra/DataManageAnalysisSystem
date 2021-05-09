package zeyfra.dmas.core.api;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import zeyfra.dmas.core.exception.ServiceException;

/**
 * 数据返回的结果封装
 * @author ZeyFra
 * @date 2021/2/16 13:14
 */
@Data
@NoArgsConstructor
@ApiModel(value="接口响应", description="接口响应")
public class ApiRest<T>{

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息")
    private String message;
    /**
     * 响应代码
     */
    @ApiModelProperty(value = "响应代码,200为成功,201为失败", required = true)
    private Integer code;

    /**
     * 请求或响应body
     */
    @ApiModelProperty(value = "响应内容")
    protected T data;

    /**
     * 是否成功
     * @return
     */
    public boolean isSuccess(){
        return code.equals(200);
    }

    /**
     * 构造函数
     * @param error
     */
    public ApiRest(ServiceException error){
        this.code = error.getCode();
        this.message = error.getMsg();
    }

    /**
     * 构造函数
     * @param error
     */
    public ApiRest(ApiError error){
        this.code = error.getCode();
        this.message = error.msg;
    }
}
