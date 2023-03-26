package cn.cc.service;

import cn.cc.dao.MemberDao;
import cn.cc.pojo.Member;
import cn.cc.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理服务接口实现类
 */

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberDao memberDao;

    //通过手机号查询会员
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //注册会员
    public void add(Member member) {
        String password = member.getPassword();
        //这里的密码如果不为空说明是通过其他表单(如完善信息)传过来的，所以这里应将密码加密一下
        if (password != null){
            //使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    //根据月份查询会员数量
    public List<Integer> findmemberCountByMonths(List<String> months) {
        //月份集合
        List<Integer> countMembers = new ArrayList<>();
        for (String month : months) {
            String date = month + ".31";
            Integer memberCount = memberDao.findMemberCountBeforeDate(date);
            countMembers.add(memberCount);
        }
        return countMembers;
    }


}
