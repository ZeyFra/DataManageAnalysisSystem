package zeyfra.dmas.modules.contest.contest.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:14
 */
@Data
public class ContestInfoDTO {

    @ApiModelProperty(value = "比赛信息")
    private ContestInfo contestInfo;

    private List<String> problemNameList;

    @ApiModelProperty(value = "比赛相关题目信息")
    private List<ContestProblemInfo> problemInfoList;

    @ApiModelProperty(value = "我院题目通过情况")
    private List<Integer> problemPassInfo;

}
