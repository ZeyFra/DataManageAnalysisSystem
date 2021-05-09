package zeyfra.dmas.modules.contest.contest.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:12
 */
@Data
public class ContestUpdateParamsDTO {

    @ApiModelProperty(value = "比赛编号")
    private Integer contestId;

    @ApiModelProperty(value = "比赛名称")
    private String contestName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "比赛日期")
    private Date contestTime;

    @ApiModelProperty(value = "比赛地点")
    private String contestSite;

    @ApiModelProperty(value = "比赛队伍数量")
    private Integer contestTeamNum;

    @ApiModelProperty(value = "比赛AK队伍个数")
    private Integer contestAkNum;

}
