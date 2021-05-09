package zeyfra.dmas.modules.now_coder_record.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zeyfra.dmas.core.api.ApiRest;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.core.controller.BaseController;
import zeyfra.dmas.modules.now_coder_record.dto.request.QueryParamsDTO;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;
import zeyfra.dmas.modules.now_coder_record.service.NowCoderRecordService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-14
 */
@RestController
@RequestMapping("dmas/api/nowCoder")
public class NowCoderRecordController extends BaseController {

    private NowCoderRecordService nowCoderRecordService;

    @Autowired
    public void setNowCoderRecordService(NowCoderRecordService nowCoderRecordService){
        this.nowCoderRecordService = nowCoderRecordService;
    }


    @ApiOperation(value = "获取牛客训练赛成绩分页list")
    @RequestMapping(value = "/pageNowCoderRecordList", method = RequestMethod.POST)
    public ApiRest<IPage<NowCoderRecord>> pageNowCoderRecordList(@RequestBody PagingReqDTO<QueryParamsDTO> reqDTO){
        //分页查询并转换
        IPage<NowCoderRecord> page = nowCoderRecordService.paging(reqDTO);
        return super.success(page);
    }

    /**
     *
     * @param paramsDTO
     * @return
     */
    @ApiOperation(value = "获取牛客训练赛成绩分页list")
    @RequestMapping(value = "/getMemeberAllNowCoderRecordForAnalysis", method = RequestMethod.GET)
    public ApiRest<List<List<String>>> getMemeberAllNowCoderRecordForAnalysis(QueryParamsDTO paramsDTO){

        List<List<String>> resultData = nowCoderRecordService.getMemeberAllNowCoderRecordForAnalysis(paramsDTO);

        return super.success(resultData);
    }
}

