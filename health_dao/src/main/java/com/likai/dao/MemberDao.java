package com.likai.dao;

import com.likai.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findByTelphone(String telephone);

    void add(Member member);

    Integer findMemberCountBeforeDate(String date);

    List<Map<String, Object>> findSetmealCount();

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
