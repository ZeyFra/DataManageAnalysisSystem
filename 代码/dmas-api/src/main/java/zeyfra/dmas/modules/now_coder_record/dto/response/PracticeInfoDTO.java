package zeyfra.dmas.modules.now_coder_record.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/15 11:03
 */
@Data
public class PracticeInfoDTO {

    @ExcelProperty(value = "序号",index = 0)
    private Integer id;

    @ExcelProperty(value = "比赛名称",index = 1)
    private String contestName;

    @ExcelProperty(value = "比赛时间",index = 2)
    private String contestTime;

    @ExcelProperty(value = "学号",index = 3)
    private String memberId;

    @ExcelProperty(value = "姓名",index = 4)
    private String memberName;

    @ExcelProperty(value = "通过题数",index = 5)
    private Integer acceptedCount;

    @ExcelProperty(value = "总题数",index = 6)
    private Integer problemCount;

    @ExcelProperty(value = "通过题数/总题数",index = 7)
    private Double theRateOfPass;

    @ExcelProperty(value = "排名",index = 8)
    private Integer rank;

    @ExcelProperty(value = "参赛人数",index = 9)
    private Integer userCount;

    @ExcelProperty(value = "排名/参赛人数",index = 10)
    private Double theRateOfRank;

}
