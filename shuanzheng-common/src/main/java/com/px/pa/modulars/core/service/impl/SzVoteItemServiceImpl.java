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

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.pa.modulars.core.entity.SzVoteItem;
import com.px.pa.modulars.core.mapper.SzVoteItemMapper;
import com.px.pa.modulars.core.service.SzVoteItemService;
import com.px.pa.modulars.core.vo.ImagesVo;
import com.px.pa.modulars.core.vo.SzVoteItemVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 投票选项
 *
 * @author pig code generator
 * @date 2021-04-13 11:25:35
 */
@Service
@Transactional
public class SzVoteItemServiceImpl extends ServiceImpl<SzVoteItemMapper, SzVoteItem> implements SzVoteItemService {

    @Resource
    private SzVoteItemMapper szVoteItemMapper;
    @Override
    public Page<SzVoteItem> getListPage(Page page,Integer vid, String key) {

        return szVoteItemMapper.getListPage(page,vid,key);
    }

    @Override
    public Map<String, Object> getranking(Integer vid, Integer viid) {
        return szVoteItemMapper.getranking(vid,viid);
    }


    @Override
    public Map<String, Object> getrankingByNumber(Integer vid, Integer viid) {
        return szVoteItemMapper.getrankingByNumber(vid,viid);
    }

    @Override
    public List<ImagesVo> getJson(String json) {
        List<ImagesVo> imagesVoArrayList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            ImagesVo imagesVo = new ImagesVo();
            String kname = object.getStr("kname");
            String kpath = object.getStr("kpath");
            String ktype = object.getStr("ktype");

            imagesVo.setKname(kname);
            imagesVo.setKpath(kpath);
            imagesVo.setKtype(ktype);
            imagesVoArrayList.add(imagesVo);
        }
        return imagesVoArrayList;
    }

    @Override
    public Integer getVoteNum(Integer vid) {
        return szVoteItemMapper.getVoteNum(vid);
    }

    @Override
    public List<SzVoteItem> getrankingList(Integer vid, Integer rank1, Integer rank2) {
        return szVoteItemMapper.getrankingList(vid,rank1,rank2);
    }

    @Override
    public List<SzVoteItemVo> listRank(Integer vid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("a.vid", vid);
        queryWrapper.orderByDesc("a.num");
        return baseMapper.listRank(queryWrapper);
    }
}
