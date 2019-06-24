package com.likai.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.likai.dao.MemberDao;
import com.likai.pojo.Member;
import com.likai.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
