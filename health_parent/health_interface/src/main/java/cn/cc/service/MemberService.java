package cn.cc.service;

import cn.cc.pojo.Member;

import java.util.List;

/**
 * 用户管理服务接口
 */

public interface MemberService {

    //通过手机号查询会员
    public Member findByTelephone(String telephone);

    //注册会员
    public void add(Member member);

    //根据月份查询会员数量
    public List<Integer> findmemberCountByMonths(List<String> months);
}
