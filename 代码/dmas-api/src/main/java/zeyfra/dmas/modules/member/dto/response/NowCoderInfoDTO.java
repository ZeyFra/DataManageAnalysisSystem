package zeyfra.dmas.modules.member.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author ZeyFra
 * @date 2021/4/12 21:14
 */
@Data
public class NowCoderInfoDTO {

    private String contestName;

    private String contestId;

    private String contestLogoUrl;

    private String teamName;

    private Integer problemCount;

    private Integer acceptedCount;

    private Integer rank;

    private Integer userCount;

    private String startTime;

    private String endTime;

    private String contestDuration;

}
