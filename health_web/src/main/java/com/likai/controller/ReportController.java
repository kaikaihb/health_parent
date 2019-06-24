package com.likai.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.likai.constant.MessageConstant;
import com.likai.entity.Result;
import com.likai.service.MemberService;
import com.likai.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            Map map = new HashMap();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);
            List<String> monthList = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                String month = DateUtils.parseDate2String(calendar.getTime(), "yyyy-MM");
                monthList.add(month);
            }
            map.put("months", monthList);
            List<Integer> memberCountList = memberService.findMemberCountByMonths(monthList);
            map.put("memberCount", memberCountList);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }
}
