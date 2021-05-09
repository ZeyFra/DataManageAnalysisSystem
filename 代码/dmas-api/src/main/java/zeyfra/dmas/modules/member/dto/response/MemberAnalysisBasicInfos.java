package zeyfra.dmas.modules.member.dto.response;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/24 16:33
 */
@Data
public class MemberAnalysisBasicInfos {

    @ApiModelProperty(value = "队员id(学号)")
    private String memberId;

    @ApiModelProperty(value = "队员姓名")
    private String memberName;

    @ApiModelProperty(value = "性别")
    private Boolean gender;

    @ApiModelProperty(value = "已退休")
    private Boolean isRetire;

    @ApiModelProperty(value = "牛客网id")
    private String nowCoderId;

    @ApiModelProperty(value = "省级奖项数")
    private Integer provincialPrizeNum;

    @ApiModelProperty(value = "国家级奖项数")
    private Integer nationalPrizeNum;

}
