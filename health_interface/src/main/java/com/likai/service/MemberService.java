package com.likai.service;

import com.likai.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);

    void register(Member member);
}
