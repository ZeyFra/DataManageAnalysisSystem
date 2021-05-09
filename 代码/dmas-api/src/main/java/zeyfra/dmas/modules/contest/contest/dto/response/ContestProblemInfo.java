package zeyfra.dmas.modules.contest.contest.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:15
 */
@Data
public class ContestProblemInfo {

    @ApiModelProperty(value = "题目名称")
    private String problemName;

    @ApiModelProperty(value = "题目的总提交次数")
    private Integer problemSubmitNum;

    @ApiModelProperty(value = "题目的pass数")
    private Integer problemPassNum;

}
