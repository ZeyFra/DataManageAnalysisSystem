package zeyfra.dmas.modules.contest.contest.dto.request;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:11
 */
@Data
public class ContestProblemInfoRequestDTO {

    @ExcelProperty(value = "题号")
    private String problemName;

    @ExcelProperty(value = "提交数")
    private Integer problemSubmitNum;

    @ExcelProperty(value = "通过数")
    private Integer problemPassNum;

}
