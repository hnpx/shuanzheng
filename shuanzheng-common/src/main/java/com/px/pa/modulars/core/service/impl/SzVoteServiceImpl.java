/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.px.pa.modulars.core.service.impl;

import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.Enum.ObjectTypeEnum;
import com.px.pa.modulars.core.entity.*;
import com.px.pa.modulars.core.mapper.SzVoteMapper;
import com.px.pa.modulars.core.service.*;
import com.px.pa.modulars.core.vo.ObjectVo;
import com.px.pa.modulars.core.vo.VoteVo;
import com.px.pa.modulars.upms.entity.SysDept;
import com.px.pa.modulars.upms.service.SysDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 投票信息
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Service
@Transactional
public class SzVoteServiceImpl extends ServiceImpl<SzVoteMapper, SzVote> implements SzVoteService {

    @Resource
    private SzVoteService szVoteService;
    @Resource
    private SzVoteItemService szVoteItemService;
    @Resource
    private SzUserService szUserService;
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SzVoteMapper szVoteMapper;
    @Resource
    private SzVoteLogService szVoteLogService;
    @Resource
    private SzVotePointsService szVotePointsService;

    @Override
    public List<ObjectVo> getObjectVo(Integer voteId, String name) {
        SzVote szVote = szVoteService.getById(voteId);
     /*   List<SzVoteItem> szVoteItemList = szVoteItemService.list(new QueryWrapper<SzVoteItem>().eq("vid", voteId));
        List<Integer> list = new ArrayList();
        szVoteItemList.forEach(szVoteItem -> {
            list.add(szVoteItem.getDid());
        });*/
        if (szVote.getType().equals(ObjectTypeEnum.ObjectTypeEnumONE.getValue())) { //村民
            // login (是否可登录：1-可登录，0-不可登录)  del_flag(是否删除  -1：已删除  0：正常)
            List<SzUser> szUserList = szUserService.list(new QueryWrapper<SzUser>().eq("del_flag", 0).and(szUserQueryWrapper -> szUserQueryWrapper
                            .like("name", name))
                    /*.notIn(list.size() > 0, "id", list)*/);
            List<ObjectVo> objectVoList = new ArrayList<>();
            szUserList.forEach(szUser -> {
                //村庄
                SysDept sysDept = sysDeptService.getById(szUser.getDid());
                String sysDeptName = null;
                if (sysDept != null) {
                    sysDeptName = sysDept.getName();
                }
                //大队
                ObjectVo objectVo = new ObjectVo();
                objectVo.setDid(szUser.getId());
                if(StringUtils.isNotEmpty(szUser.getPhone())){
                    objectVo.setName(szUser.getName() + "-" + szUser.getPhone() + "-" + sysDeptName);
                }else {
                    objectVo.setName(szUser.getName() + "-" + ""+ "-" + sysDeptName);
                }

                objectVoList.add(objectVo);
            });
            return objectVoList;
        } else if (szVote.getType().equals(ObjectTypeEnum.ObjectTypeEnumTWO.getValue())) { //村庄
            // del_flag(是否删除  -1：已删除  0：正常)
            List<SysDept> sysDeptList = sysDeptService.list(new QueryWrapper<SysDept>().eq("del_flag", 0).eq("parent_id", 1)
                            .like("name", name)
                    /* .notIn(list.size() > 0, "dept_id", list)*/);
            List<ObjectVo> objectVoList = new ArrayList<>();
            sysDeptList.forEach(sysDept -> {
                ObjectVo objectVo = new ObjectVo();
                objectVo.setDid(sysDept.getDeptId());
                objectVo.setName(sysDept.getName());
                objectVoList.add(objectVo);
            });
            return objectVoList;
        }
        return null;
    }


    /**
     * 时间差
     *
     * @return
     */
    @Override
    public int getTime(Date startTime, Date endTime) {
        //Date date = new Date();
        // Date startDate = Date.from(startTime.toInstant(ZoneOffset.of("+8")));
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");//如2016-08-10 20:40
        String fromDate = simpleFormat.format(startTime);
        String toDate = simpleFormat.format(endTime);
        long from = 0;
        try {
            from = simpleFormat.parse(fromDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long to = 0;
        try {
            to = simpleFormat.parse(toDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int minutes = (int) ((to - from) / (1000 * 60));
        return minutes;
    }

    @Override
    public Page<SzVote> getPageList(Page page, String name, Integer type) {

        return szVoteMapper.getPageList(page, name, type);
    }


    @Override
    public void run(VoteVo voteVo) {
        //获取手机的ip地址
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        synchronized (this) {
            SzVoteLog szVoteLog1 = new SzVoteLog();
            szVoteLog1.setAvatar(voteVo.getAvatar());
            szVoteLog1.setOpenid(voteVo.getOpenid());
            szVoteLog1.setNickname(voteVo.getNickname());
            szVoteLog1.setVid(voteVo.getVid());
            szVoteLog1.setViid(voteVo.getViid());
            szVoteLog1.setCreateTime(LocalDateTime.now());
            szVoteLog1.setIp(ServletUtil.getClientIP(request));
            //记录投票数
            SzVoteItem szVoteItem = szVoteItemService.getById(voteVo.getViid());
            if (szVoteItem.getNum() == null || szVoteItem.getNum() == 0) {
                szVoteItem.setNum(1);
            } else {
                szVoteItem.setNum(szVoteItem.getNum() + 1);
            }
            szVoteItem.setUpdateTime(LocalDateTime.now());
            szVoteItemService.updateById(szVoteItem);
            szVoteLogService.save(szVoteLog1);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run1(Integer id, Integer state) {
        synchronized (this) {
            SzVote szVote = szVoteService.getById(id);
            if (szVote != null) {
                if (state == 1) {
                    szVote.setViews(szVote.getViews() + 1);
                }
            }
            szVoteService.updateById(szVote);
        }
    }

    @Override
    public void issue(Integer vid) {
        SzVote szVote = szVoteService.getById(vid);
        if (szVote.getType().equals(ObjectTypeEnum.ObjectTypeEnumONE.getValue())) {
            List<SzVotePoints> szVotePointsList = szVotePointsService.list(new QueryWrapper<SzVotePoints>().eq("vid", vid));
            szVotePointsList.forEach(szVotePoints -> {
                List<SzVoteItem> szVoteItemList = szVoteItemService.getrankingList(vid, szVotePoints.getStart(), szVotePoints.getEnd());
                szVoteItemList.forEach(szVoteItem -> {
                    if (szVoteItem.getNum() > 0) {
                        SzUser szUser = szUserService.getById(szVoteItem.getDid());
                        //历史总积分
                        if (szUser.getAllScore() == null || szUser.getAllScore() == 0) {
                            szUser.setAllScore(szVotePoints.getPoints());
                        } else {
                            szUser.setAllScore(szUser.getAllScore() + szVotePoints.getPoints());
                        }
                        //剩余积分
                        if (szUser.getScore() == null || szUser.getScore() == 0) {
                            szUser.setScore(szVotePoints.getPoints());
                        } else {
                            szUser.setScore(szUser.getScore() + szVotePoints.getPoints());
                        }
                        szUserService.updateById(szUser);
                    }
                });
            });
        } else {
            List<SzVotePoints> szVotePointsList = szVotePointsService.list(new QueryWrapper<SzVotePoints>().eq("vid", vid));
            szVotePointsList.forEach(szVotePoints -> {
                List<SzVoteItem> szVoteItemList = szVoteItemService.getrankingList(vid, szVotePoints.getStart(), szVotePoints.getEnd());
                szVoteItemList.forEach(szVoteItem -> {
                    if (szVoteItem.getNum() > 0) {
                        SysDept sysDept = sysDeptService.getById(szVoteItem.getDid());
                        //历史总积分
                        if (sysDept.getAllScore() == null || sysDept.getAllScore() == 0) {
                            sysDept.setAllScore(szVotePoints.getPoints());
                        } else {
                            sysDept.setAllScore(sysDept.getAllScore() + szVotePoints.getPoints());
                        }
                        //剩余积分
                        if (sysDept.getScore() == null || sysDept.getScore() == 0) {
                            sysDept.setScore(szVotePoints.getPoints());
                        } else {
                            sysDept.setScore(sysDept.getScore() + szVotePoints.getPoints());
                        }
                        sysDeptService.updateById(sysDept);
                    }
                });
            });
        }
        //更新积分发放记录
        szVote.setIntegralStatus(1);
        szVoteService.updateById(szVote);


    }


}
