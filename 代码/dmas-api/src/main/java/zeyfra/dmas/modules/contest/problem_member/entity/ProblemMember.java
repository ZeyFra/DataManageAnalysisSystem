package zeyfra.dmas.modules.contest.problem_member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 题目队员关系表
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProblemMember对象", description="题目队员关系表")
public class ProblemMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目编号")
    private Integer problemId;

    @ApiModelProperty(value = "队员编号(学号)")
    private String memberId;

    @ApiModelProperty(value = "答题情况")
    private String result;


}
