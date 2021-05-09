package zeyfra.dmas.modules.member.dto.request;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ZeyFra
 * @date 2021/4/11 22:13
 */
@Data
public class MemberUploadDataDTO {

    @ExcelProperty(value = "学号", index = 0)
    private String memberId;

    @ExcelProperty(value = "姓名", index = 1)
    private String memberName;

    @ExcelProperty(value = "性别", index = 2)
    private Boolean gender;

    @ExcelProperty(value = "电话", index = 3)
    private String telephone;

    @ExcelProperty(value = "QQ", index = 4)
    private String qq;

    @ExcelProperty(value = "牛客网ID", index = 5)
    private String nowCoderId;


}
