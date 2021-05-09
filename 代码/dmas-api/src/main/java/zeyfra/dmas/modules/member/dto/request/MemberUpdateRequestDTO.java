package zeyfra.dmas.modules.member.dto.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ZeyFra
 * @date 2021/4/11 18:26
 */
@Data
public class MemberUpdateRequestDTO {

    @ApiModelProperty(value = "队员id(学号)")
    private String memberId;

    @ApiModelProperty(value = "队员姓名")
    private String memberName;

    @ApiModelProperty(value = "性别")
    private Boolean gender;

    @ApiModelProperty(value = "电话号码")
    private String telephone;

    @ApiModelProperty(value = "QQ号")
    private String qq;

    @ApiModelProperty(value = "已退休")
    private Boolean isRetire;

    @ApiModelProperty(value = "牛客网id")
    private String newcoderId;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
