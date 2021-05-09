package zeyfra.dmas.modules.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.modules.member.dto.request.MemberRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberSearchRequestDTO;
import zeyfra.dmas.modules.member.dto.request.MemberUpdateRequestDTO;
import zeyfra.dmas.modules.member.dto.response.*;
import zeyfra.dmas.modules.member.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
public interface MemberService extends IService<Member> {

    /**
     * 分页查询数据
     * @param reqDTO
     * @return
     */
    IPage<Member> paging(PagingReqDTO<Member> reqDTO);

    /**
     * 模糊查询
     * @param  reqDTO
     * @return IPage<User>
     */
    IPage<Member> queryMemberByMemberId(PagingReqDTO<MemberSearchRequestDTO> reqDTO);

    /**
     * 更新队员信息
     * @param reqDTO
     * @return
     */
    boolean updateMember(MemberUpdateRequestDTO reqDTO);

    /**
     * 上传队员数据
     * @param file
     * @return
     */
    boolean uploadMember(MultipartFile file);

    /**
     * 爬出牛客网信息
     * @param nowCoderId
     * @return
     * @throws IOException
     */
    List<NowCoderInfoDTO> crawNowCoderInfo(String nowCoderId) throws IOException;

    /**
     * 获取学生考试成绩
     * @param memberId
     * @param memberName
     * @return
     */
    List<ExamScoreInfoDTO> getExamDetailList(String memberId,String memberName);

    List<NowCoderRecord> getNowCoderDetailByMemberId(String memberId);

    /**
     * 更新所有成员牛客网训练赛情况
     * @return
     */
    boolean updateAllNowCoderRecord() throws IOException;

    MemberAnalysisPage pagingMemberAnalysisInfo(PagingReqDTO<Member> reqDTO);

    MemberAnalysisPage queryMemberAnalysisByMemberId(PagingReqDTO<MemberSearchRequestDTO> reqDTO);

    MemberAnalysisDetailInfos getMemberAnalysisInfos(MemberRequestDTO reqDTO);

    List<MemberDistributionDTO> getAllMemeberDistribution();

    AllMemberExamInfoDTO getMemeberAllExamDetailForAnalysis();
}
