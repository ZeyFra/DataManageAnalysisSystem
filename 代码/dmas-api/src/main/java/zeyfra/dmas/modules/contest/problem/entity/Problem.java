package zeyfra.dmas.modules.contest.problem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 题目表
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Problem对象", description="题目表")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目编号")
    @TableId(value = "problem_id", type = IdType.AUTO)
    private Integer problemId;

    @ApiModelProperty(value = "题目名称")
    private String problemName;

    @ApiModelProperty(value = "题目的总提交次数")
    private Integer problemSubmitNum;

    @ApiModelProperty(value = "题目的pass数")
    private Integer problemPassNum;


}
