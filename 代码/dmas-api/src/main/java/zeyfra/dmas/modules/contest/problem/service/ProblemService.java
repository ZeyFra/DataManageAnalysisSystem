package zeyfra.dmas.modules.contest.problem.service;

import zeyfra.dmas.modules.contest.problem.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 题目表 服务类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
public interface ProblemService extends IService<Problem> {

    List<Integer> problemPassInfo(String contestId);
}
