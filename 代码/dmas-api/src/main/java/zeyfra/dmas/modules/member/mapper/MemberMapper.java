package zeyfra.dmas.modules.member.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import zeyfra.dmas.modules.member.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 分页
     * @param page
     * @return
     */
    IPage<Member> paging(Page page);

    /**
     * 根据学号和状态查找分页内容
     * @param page
     * @param memberId
     * @param isRetire
     * @return
     */
    IPage<Member> queryMemberByMemberIdAndStae(Page page,String memberId, Integer isRetire);

    /**
     * 根据学号查找分页内容
     * @param page
     * @param memberId
     * @return
     */
    IPage<Member> queryMemberByMemberId(Page page,String memberId);

    /**
     * 根据状态查找分页内容
     * @param page
     * @param isRetire
     * @return
     */
    IPage<Member> queryMemberByState(Page page,Integer isRetire);

}
