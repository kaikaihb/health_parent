package com.likai.service;

import com.likai.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void register(Member member);

    List<Integer> findMemberCountByMonths(List<String> monthList);

    List<Map<String, Object>> findSetmealCount();
}
