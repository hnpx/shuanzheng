package com.px.pa.utils.bean;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体类转换为VO类
 *
 * @author zhouz
 */
public class BeanToResultUtil {

    /**
     * 转换实体类到VO类
     *
     * @param bean
     * @param rClass
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> R beanToResult(T bean, Class<R> rClass) {
        R r = null;
        try {
            r = rClass.newInstance();
            BeanUtil.copyProperties(bean, r);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return r;
    }

    public static <R, T> List<R> beanToResult(List<T> list, Class<R> rClass) {
        List<R> results = new ArrayList<>();
        for (T t : list) {
            R r = beanToResult(t, rClass);
            results.add(r);
        }
        return results;
    }

    public static <R, T> Page<R> beanToResult(Page<T> page, Class<R> rClass) {
        Page<R> pageResult = new Page<>();
        pageResult.setSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setPages(page.getPages());
        pageResult.setRecords(beanToResult(page.getRecords(), rClass));
        return pageResult;
    }
}
