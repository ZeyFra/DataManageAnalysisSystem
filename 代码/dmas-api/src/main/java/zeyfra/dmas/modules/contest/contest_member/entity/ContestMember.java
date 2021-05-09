package zeyfra.dmas.modules.contest.contest_member.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 比赛队员关系表
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ContestMember对象", description="比赛队员关系表")
public class ContestMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "比赛编号")
    private Integer contestId;

    @ApiModelProperty(value = "用户编号")
    private String memberId;

    @ApiModelProperty(value = "rank")
    private String rank;


}
