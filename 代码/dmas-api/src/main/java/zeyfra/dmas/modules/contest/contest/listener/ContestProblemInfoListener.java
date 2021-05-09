package zeyfra.dmas.modules.contest.contest.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zeyfra.dmas.modules.contest.contest_member.service.impl.ContestMemberServiceImpl;
import zeyfra.dmas.modules.contest.contest_problem.entity.ContestProblem;
import zeyfra.dmas.modules.contest.contest_problem.service.ContestProblemService;
import zeyfra.dmas.modules.contest.contest_problem.service.impl.ContestProblemServiceImpl;
import zeyfra.dmas.modules.contest.problem.entity.Problem;
import zeyfra.dmas.modules.contest.problem_member.service.ProblemMemberService;
import zeyfra.dmas.modules.contest.problem_member.service.impl.ProblemMemberServiceImpl;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestDataDTO;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestProblemInfoRequestDTO;
import zeyfra.dmas.modules.contest.problem.service.ProblemService;
import zeyfra.dmas.modules.contest.problem.service.impl.ProblemServiceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static zeyfra.dmas.core.utils.ObjectUtils.checkObjectFieldIsNull;

/**
 * @author ZeyFra
 * @date 2021/4/11 20:19
 */
public class ContestProblemInfoListener extends AnalysisEventListener<ContestProblemInfoRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContestProblemInfoListener.class);

    private ProblemService problemService;
    private ProblemMemberService problemMemberService;
    private ContestProblemService contestProblemService;
    private InputStream file;
    private Integer contestId;

    public ContestProblemInfoListener(ProblemServiceImpl problemService, ProblemMemberService problemMemberService, ContestProblemServiceImpl contestProblemService, InputStream file, Integer contestId) {
        this.problemService = problemService;
        this.problemMemberService = problemMemberService;
        this.contestProblemService = contestProblemService;
        this.file = file;
        this.contestId = contestId;
    }

    private List<ContestProblemInfoRequestDTO> list = new ArrayList<ContestProblemInfoRequestDTO>();
    private List<Problem> problemList = new ArrayList<>();
    private List<ContestProblem> contestProblemList = new ArrayList<>();

    @SneakyThrows
    @Override
    public void invoke(ContestProblemInfoRequestDTO data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}",data.toString());
        if(!checkObjectFieldIsNull(data)){
            list.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());

        // 保存题目信息,随后获得题目id
        for (ContestProblemInfoRequestDTO contestProblemInfoRequestDTO : list) {
            Problem problem = new Problem();
            problem.setProblemName(contestProblemInfoRequestDTO.getProblemName());
            problem.setProblemPassNum(contestProblemInfoRequestDTO.getProblemPassNum());
            problem.setProblemSubmitNum((contestProblemInfoRequestDTO.getProblemSubmitNum()));
            problemList.add(problem);
        }
        problemService.saveBatch(problemList);

        // 依据题目id和比赛id保存题目比赛信息
        for (int i = 0;i < list.size();i++){
            ContestProblem contestProblem = new ContestProblem();
            contestProblem.setContestId(contestId);
            contestProblem.setProblemId(problemList.get(i).getProblemId());
            contestProblemList.add(contestProblem);
        }
        contestProblemService.saveBatch(contestProblemList);

        // 读sheet0保存比赛用户信息的同时，传入所有的题目id以此保存用户题目信息，
        EasyExcel.read(file, ContestDataDTO.class,new ContestDataListener(new ContestMemberServiceImpl(), new ProblemMemberServiceImpl(), problemList, contestId)).sheet(0).doRead();
        // 保存题目和用户的信息
        contestProblemList.clear();
        problemList.clear();
    }

}
