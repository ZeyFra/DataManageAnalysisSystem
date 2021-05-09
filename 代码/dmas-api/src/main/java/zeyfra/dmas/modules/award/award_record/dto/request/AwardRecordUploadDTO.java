package zeyfra.dmas.modules.award.award_record.dto.request;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author ZeyFra
 * @date 2021/4/20 22:04
 */
@Data
public class AwardRecordUploadDTO {

    @ExcelProperty(value = "组织单位", index = 1)
    private String organizationalUnit;

    @ExcelProperty(value = "获奖年度", index = 2)
    private String awardYear;

    @ExcelProperty(value = "获奖时间", index = 3)
    private Date awardTime;

    @ExcelProperty(value = "获奖类型简写", index = 4)
    private String awardType;

    @ExcelProperty(value = "获奖名称", index = 5)
    private String awardName;

    @ExcelProperty(value = "级别（国/省）", index = 6)
    private String awardLevel;

    @ExcelProperty(value = "证书获奖等级", index = 7)
    private String certificateLevel;

    @ExcelProperty(value = "证书获奖等级", index = 8)
    private String theSchoolRecognizesTheAwardLevel;

    @ExcelProperty(value = "学校认定获奖等级", index = 9)
    private String awardingUnit;

    @ExcelProperty(value = "获奖学生", index = 10)
    private String awardMember;

}
