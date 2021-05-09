package zeyfra.dmas.modules.now_coder_record.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import zeyfra.dmas.core.api.PagingReqDTO;
import zeyfra.dmas.modules.now_coder_record.dto.request.QueryParamsDTO;
import zeyfra.dmas.modules.now_coder_record.entity.NowCoderRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-14
 */
public interface NowCoderRecordService extends IService<NowCoderRecord> {

    /**
     * 获取牛客训练赛成绩分页list
     * @return ApiRest<IPage<NowCoderRecord>>
     */
    IPage<NowCoderRecord> paging(PagingReqDTO<QueryParamsDTO> reqDTO);

    List<List<String>> getMemeberAllNowCoderRecordForAnalysis(QueryParamsDTO paramsDTO);
}
