package com.px.pa.modulars.core.task;

import com.px.basic.alone.security.job.DelayTask;
import com.px.basic.alone.security.job.TaskBase;
import com.px.pa.SpringContextUtil;
import com.px.pa.modulars.core.controller.SzTaskController;
import org.springframework.beans.factory.annotation.Autowired;

public class PointsTask extends DelayTask {

    @Autowired
    protected SzTaskController szTaskController = (SzTaskController) SpringContextUtil.getBean("szTaskController");

    public PointsTask(TaskBase data, long expire) {
        super(data, expire);
    }

    @Override
    public void execute(TaskBase taskBase) {
        String key= taskBase.getIdentifier();
        if(key.startsWith(PointsTaskSign.start_points_task)){
            Integer id=Integer.valueOf(key.replace(PointsTaskSign.start_points_task,""));
            szTaskController.updateState(id,1,null);
        }else if(key.startsWith(PointsTaskSign.end_points_task)){
            Integer id=Integer.valueOf(key.replace(PointsTaskSign.end_points_task,""));
            szTaskController.updateState(id,2,"任务过期，自动关闭任务");
        }
    }
}
