package com.likai.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.likai.constant.MessageConstant;
import com.likai.dao.MemberDao;
import com.likai.dao.OrderDao;
import com.likai.dao.OrderSettingDao;
import com.likai.entity.Result;
import com.likai.pojo.Member;
import com.likai.pojo.Order;
import com.likai.pojo.OrderSetting;
import com.likai.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result submit(Map map) throws Exception {
        /**
         * 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
         * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
         * 3、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
         * 4、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
         * 5、预约成功，更新当日的已预约人数
         */
        String setmealId = (String) map.get("setmealId");
        String telephone = (String) map.get("telephone");
        String orderDateStr = (String) map.get("orderDate");
        Date orderDate = DateUtils.parseString2Date(orderDateStr);
        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        if (orderSetting.getReservations() == orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        // 3.判断是否是已注册
        Member member = memberDao.findByTelphone(telephone);
        if (member == null) {
            //注册member
            member = new Member();
            String name = (String) map.get("name");
            member.setName(name);
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberDao.add(member);
        }else {
            //防止重复预约
            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(orderDate);
            order.setSetmealId(Integer.parseInt(setmealId));
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }

        //直接预约,向order表中添加一条记录
        Order order = new Order(member.getId(), new Date(), Order.ORDERTYPE_WEIXIN, Order.ORDERSTATUS_NO, Integer.parseInt(setmealId));
        orderDao.add(order);
        //orderSetting表中的reservations加一
        orderSettingDao.editReservationsByOrderDate(orderDate);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById4Detail(id);
        return map;
    }
}
