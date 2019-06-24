package com.likai.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.likai.dao.MemberDao;
import com.likai.pojo.Member;
import com.likai.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelphone(telephone);
    }

    @Override
    public void register(Member member) {
        if(member.getPassword() != null && !"".equals(member.getPassword())){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> monthList) {
        List<Integer> memberCountList = new ArrayList<Integer>();
        for (String date : monthList) {
            date = date+"-31";
            Integer memberCount = memberDao.findMemberCountBeforeDate(date);
            memberCountList.add(memberCount);
        }
        return memberCountList;
    }
}
