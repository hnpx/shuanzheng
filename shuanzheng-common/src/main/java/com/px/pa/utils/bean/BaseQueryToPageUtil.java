package com.px.pa.utils.bean;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.core.vo.BasePageParamVo;

public class BaseQueryToPageUtil {

    /**
     * 默认页码
     */
    public static final int PAGE_NO = 1;
    /**
     * 默认每页的条数
     */
    public static final int PAGE_SIZE = 20;

    /**
     * 获得对应的PAGE信息
     *
     * @param param
     * @param <T>
     * @return
     */
    public static <T> Page<T> createPage(BasePageParamVo param) {
        if(param.getPageNo()==null){
            if(param.getPage()==null){
                param.setPageNo(PAGE_NO);
            }else{
                param.setPageNo(param.getPage());
            }
        }
        if(param.getPageSize() == null){
            if(param.getLimit()==null){
                param.setPageSize(PAGE_SIZE);
            }else{
                param.setPageSize(param.getLimit());
            }
        }
        Page<T> page = new Page<>();
        page.setSize(param.getPageSize());
        page.setCurrent(param.getPageNo());
        return page;
    }
}
