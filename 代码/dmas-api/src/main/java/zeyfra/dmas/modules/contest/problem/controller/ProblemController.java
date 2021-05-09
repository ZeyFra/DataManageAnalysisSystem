package zeyfra.dmas.modules.contest.problem.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zeyfra.dmas.core.api.ApiRest;
import zeyfra.dmas.core.controller.BaseController;
import zeyfra.dmas.modules.contest.contest.dto.request.ContestInfoRequestDTO;
import zeyfra.dmas.modules.contest.problem.service.ProblemService;

import java.util.List;

/**
 * <p>
 * 题目表 前端控制器
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@RestController
@RequestMapping("/dmas/api/problem")
public class ProblemController extends BaseController {


}

