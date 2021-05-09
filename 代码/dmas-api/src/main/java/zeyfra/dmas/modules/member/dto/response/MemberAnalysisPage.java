package zeyfra.dmas.modules.member.dto.response;

import lombok.Data;

import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/24 16:32
 */
@Data
public class MemberAnalysisPage {

    private long current;
    private long size;
    private long total;
    private List<MemberAnalysisBasicInfos> records;

}
