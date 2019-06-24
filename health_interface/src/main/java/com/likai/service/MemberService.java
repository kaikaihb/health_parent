package com.likai.service;

import com.likai.pojo.Member;

import java.util.List;

public interface MemberService {
    Member findByTelephone(String telephone);

    void register(Member member);

    List<Integer> findMemberCountByMonths(List<String> monthList);
}
