package zeyfra.dmas.modules.member.dto.response;

import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/26 19:55
 */
@Data
public class MemberDistributionDTO {

    private String name;
    private Integer value;

    public MemberDistributionDTO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
