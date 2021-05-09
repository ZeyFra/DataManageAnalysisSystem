package zeyfra.dmas.modules.contest.contest.dto.request;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:08
 */
@Data
public class ContestDataDTO {

    @ExcelProperty(value = "排名")
    public String rank;
    @ExcelProperty(value = "序号")
    private String id;
    @ExcelProperty(value = "学号")
    private String memberId;
    @ExcelProperty(value = "姓名")
    private String memberName;
    @ExcelProperty(value = "A")
    private String a="";
    @ExcelProperty(value = "B")
    private String b="";
    @ExcelProperty(value = "C")
    private String c="";
    @ExcelProperty(value = "D")
    private String d="";
    @ExcelProperty(value = "E")
    private String e="";
    @ExcelProperty(value = "F")
    private String f="";
    @ExcelProperty(value = "G")
    private String g="";
    @ExcelProperty(value = "H")
    private String h="";
    @ExcelProperty(value = "I")
    private String i="";
    @ExcelProperty(value = "J")
    private String j="";
    @ExcelProperty(value = "K")
    private String k="";
    @ExcelProperty(value = "L")
    private String l="";
    @ExcelProperty(value = "M")
    private String m="";
    @ExcelProperty(value = "N")
    private String n="";
    @ExcelProperty(value = "O")
    private String o="";

}
