package zeyfra.dmas.modules.contest.contest.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:10
 */
@Data
public class ContestUploadInfoRequestDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "比赛名称")
    private String contestName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "比赛日期")
    private Date contestTime;

    @ApiModelProperty(value = "比赛地点")
    private String contestSite;

    @ApiModelProperty(value = "比赛队伍数量")
    private Integer contestTeamNum;

    @ApiModelProperty(value = "比赛AK队伍个数")
    private Integer contestAkNum;

}
