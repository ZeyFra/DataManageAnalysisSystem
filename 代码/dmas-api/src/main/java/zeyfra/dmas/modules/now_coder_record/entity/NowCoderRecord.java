package zeyfra.dmas.modules.now_coder_record.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-14
 */
@Data
@ApiModel(value="NowCoderRecord对象")
public class NowCoderRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "编号", index = 0)
    @TableId(value = "now_coder_record_id", type = IdType.AUTO)
    private Integer nowCoderRecordId;

    @ExcelProperty(value = "比赛名称", index = 1)
    private String contestName;

    @ExcelIgnore
    private String contestId;

    @ExcelIgnore
    private String logoUrl;

    @ExcelIgnore
    private String teamName;

    @ExcelIgnore
    private String memberId;

    @ExcelProperty(value = "总题数", index = 2)
    private Integer problemCount;

    @ExcelProperty(value = "通过题数", index = 3)
    private Integer acceptCount;

    @ExcelProperty(value = "排名", index = 4)
    private Integer rank;

    @ExcelProperty(value = "参赛人数", index = 5)
    private Integer userCount;

    @ExcelProperty(value = "比赛开始时间", index = 6)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private String startTime;

    @ExcelProperty(value = "比赛结束时间", index = 7)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private String endTime;

    @ExcelProperty(value = "比赛时长（小时）", index = 8)
    private String contestDuration;

}
