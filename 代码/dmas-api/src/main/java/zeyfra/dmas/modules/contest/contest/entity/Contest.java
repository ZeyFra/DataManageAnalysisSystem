package zeyfra.dmas.modules.contest.contest.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 比赛表
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Contest对象", description="比赛表")
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "比赛编号")
    @TableId(value = "contest_id", type = IdType.AUTO)
    private Integer contestId;

    @ApiModelProperty(value = "比赛名称")
    private String contestName;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty(value = "比赛日期")
    private Date contestTime;

    @ApiModelProperty(value = "比赛地点")
    private String contestSite;

    @ApiModelProperty(value = "比赛队伍数量")
    private Integer contestTeamNum;

    @ApiModelProperty(value = "比赛AK队伍个数")
    private Integer contestAkNum;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建数据")
    private Date createdTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
