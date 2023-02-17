package cn.cc.dao;

import com.github.pagehelper.Page;
import cn.cc.pojo.Member;

import java.util.List;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);

    //自动注册会员
    public void add(Member member);

    public void deleteById(Integer id);

    public Member findById(Integer id);

    //根据手机号查找会员信息
    public Member findByTelephone(String telephone);
    public void edit(Member member);

    //根据当前日期查询前n个日期的会员数量
    public Integer findMemberCountBeforeDate(String date);

    //根据日期统计会员数
    public Integer findMemberCountByDate(String date);

    //根据当前日期查询后n个日期的会员数量
    public Integer findMemberCountAfterDate(String date);

    //查询总会员数
    public Integer findMemberTotalCount();
}
