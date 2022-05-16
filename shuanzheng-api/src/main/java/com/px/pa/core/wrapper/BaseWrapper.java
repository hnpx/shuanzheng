package com.px.pa.core.wrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.support.page.Pagination;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据处理
 */
@Component
public abstract class BaseWrapper<T,VO> {

    /**
     *
     * @return
     */
    public List<VO> wraps(List<T> list){
       List<VO> vos=new ArrayList<>();
       for(T t:list){
           vos.add(this.wrap(t));
       }
       return vos;
    }

    public abstract VO wrap(T item);

    public Page<VO> getVoPage(Page<T> page){
        Page<VO> p=new Page<>();
        p.setTotal(page.getTotal());
        p.setCurrent(page.getCurrent());
        p.setSize(page.getSize());
        p.setPages(page.getPages());
        p.setRecords(this.wraps(page.getRecords()));
        return p;
    }
    public Pagination<VO> getVoPage(Pagination<T> page){
        Pagination<VO> p=new Pagination<>();
        p.setTotal(page.getTotal());
        p.setCurrent(page.getCurrent());
        p.setSize(page.getSize());
//        p.set
//        p.setPages(page.getPages());
        p.setRecords(this.wraps(page.getRecords()));
        return p;
    }
}
