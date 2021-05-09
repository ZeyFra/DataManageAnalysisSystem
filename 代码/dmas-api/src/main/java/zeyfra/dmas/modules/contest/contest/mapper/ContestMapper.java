package zeyfra.dmas.modules.contest.contest.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zeyfra.dmas.modules.contest.problem.entity.Problem;
import zeyfra.dmas.modules.contest.problem_member.entity.ProblemMember;
import zeyfra.dmas.modules.contest.contest.entity.Contest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import zeyfra.dmas.modules.member.entity.Member;

import java.util.List;

/**
 * <p>
 * 比赛表 Mapper 接口
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
public interface ContestMapper extends BaseMapper<Contest> {

    /**
     * 查找分页内容
     * @param page
     * @return
     */
    IPage<Contest> paging(Page page);

    List<Problem> getProblemList(String contestId);

    Integer getMyTeamNum(String contestId);

    List<String> getContestProblemName(String contestId);

    List<Member> getContestMemberList(String contestId);

    ProblemMember getProblemMember(Integer problemId, String memberId);

    String getRank(String contestId, String member_id);

    IPage<Contest> queryContest(Page page,String contestName, String contestTime);

    IPage<Contest> queryContestByContestTime(Page page, String contestTime);

    IPage<Contest> queryContestByContestName(Page page,String contestName);

}
