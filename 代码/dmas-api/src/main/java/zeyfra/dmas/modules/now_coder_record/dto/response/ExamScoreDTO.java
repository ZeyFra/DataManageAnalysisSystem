package zeyfra.dmas.modules.now_coder_record.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/15 11:03
 */
@Data
public class ExamScoreDTO {

    @ExcelProperty(value = "学号",index = 0)
    private String memberId;

    @ExcelProperty(value = "姓名",index = 1)
    private String memberName;

    @ExcelProperty(value = "课程编号",index = 2)
    private String courseId;

    @ExcelProperty(value = "课程名称",index = 3)
    private String courseName;

    @ExcelProperty(value = "总成绩",index = 4)
    private Double score;

    @ExcelProperty(value = "课程性质",index = 5)
    private String courseType;

    @ExcelProperty(value = "课程属性",index = 6)
    private String courseProperty;

    @ExcelProperty(value = "学时",index = 7)
    private String courseTime;

    @ExcelProperty(value = "学分",index = 8)
    private Double courseRank;

}
