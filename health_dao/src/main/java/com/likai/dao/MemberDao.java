package com.likai.dao;

import com.likai.pojo.Member;

public interface MemberDao {
    Member findByTelphone(String telephone);

    void add(Member member);
}
