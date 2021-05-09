package zeyfra.dmas.modules.contest.problem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import zeyfra.dmas.modules.contest.contest.service.ContestService;
import zeyfra.dmas.modules.contest.contest_problem.entity.ContestProblem;
import zeyfra.dmas.modules.contest.contest_problem.service.ContestProblemService;
import zeyfra.dmas.modules.contest.problem.entity.Problem;
import zeyfra.dmas.modules.contest.problem.mapper.ProblemMapper;
import zeyfra.dmas.modules.contest.problem.service.ProblemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import zeyfra.dmas.modules.contest.problem_member.entity.ProblemMember;
import zeyfra.dmas.modules.contest.problem_member.service.ProblemMemberService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 题目表 服务实现类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    private ProblemMemberService problemMemberService;
    private ContestProblemService contestProblemService;


    @Autowired
    public void setProblemMemberService(ProblemMemberService problemMemberService) {
        this.problemMemberService = problemMemberService;
    }

    @Autowired
    public void setContestProblemService(ContestProblemService contestProblemService) {
        this.contestProblemService = contestProblemService;
    }




    @Override
    public List<Integer> problemPassInfo(String contestId) {
        List<Integer> result = new ArrayList<>();
        int passNum = 0;

        QueryWrapper<ContestProblem> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ContestProblem::getContestId,contestId);
        System.out.println(contestId);
        List<ContestProblem> problemList = contestProblemService.list(wrapper);

        for (ContestProblem item : problemList) {

            QueryWrapper<ProblemMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(ProblemMember::getProblemId,item.getProblemId());
            queryWrapper.lambda().orderByAsc(ProblemMember::getId);
            List<ProblemMember> problemMemberList = problemMemberService.list(queryWrapper);

            for (ProblemMember problemMember : problemMemberList) {
                if (problemMember.getResult() != null){
                    if(!"".equals(problemMember.getResult())){
                        passNum++;
                    }
                }
            }
            result.add(passNum);
            passNum = 0;
        }

        return result;
    }

}
