package zeyfra.dmas.modules.contest.contest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestUpdateParamsDTO;
import zeyfra.dmas.modules.contest.contest.dto.response.ContestMemberColumnDTO;
import zeyfra.dmas.modules.contest.contest.dto.response.ContestMemberDetail;
import zeyfra.dmas.modules.contest.contest.entity.Contest;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestDeleteParamsDTO;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestQueryParamsDTO;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestUploadInfoRequestDTO;
import zeyfra.dmas.modules.contest.contest.dto.response.ContestInfoDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 比赛表 服务类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
public interface ContestService extends IService<Contest> {

    /**
     * 分页查询数据
     * @param reqDTO
     * @return IPage<Contest>
     */
    IPage<Contest> paging(PagingReqDTO<Contest> reqDTO);

    /**
     * 获取比赛信息
     * @param contestId
     * @return ContestInfoDTO
     */
    ContestInfoDTO getContestInfo(String contestId);

    /**
     * 获取某场比赛详情页动态得table column
     * @param contestId
     * @return List<ContestMemberColumnDTO>
     */
    List<ContestMemberColumnDTO> getContestMemberColumnLsit(String contestId);

    /**
     * 获取某场比赛详情页得数据List
     * @param contestId
     * @return List<ContestMemberDetail>
     */
    List<ContestMemberDetail> getContestMemberDetailList(String contestId);

    /**
     * 根据条件查询比赛
     * @param reqDTO
     * @return IPage<Contest>
     */
    IPage<Contest> queryContest(PagingReqDTO<ContestQueryParamsDTO> reqDTO);

    /**
     * 修改比赛信息
     * @param reqDTO
     * @return IPage<Contest>
     */
    Boolean updateContest(ContestUpdateParamsDTO reqDTO);

    /**
     * 修改比赛信息
     * @param paramsDTO
     * @return IPage<Contest>
     */
    Boolean deleteContest(ContestDeleteParamsDTO paramsDTO);

    /**
     * 修改比赛信息
     * @param reqDTO
     * @param file
     * @return IPage<Contest>
     */
    Boolean uploadContest(ContestUploadInfoRequestDTO reqDTO, MultipartFile file);

}
