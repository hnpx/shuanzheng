package com.px.pa.modulars.home.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface HomeInfoMapper {

    @Select("SELECT dept.`name` as name,count(su.id) as num FROM sz_user su LEFT JOIN sys_dept dept ON dept.dept_id=su.did WHERE su.del_flag=0 GROUP BY dept.`name`")
    public List<Map<String, Object>> countPeopleInfo();

    @Select("SELECT dept.`name` as name,count(su.id) as num FROM sz_user su LEFT JOIN sys_dept dept ON dept.dept_id=su.did WHERE su.login=1 AND su.del_flag=0 GROUP BY dept.`name`")
    public List<Map<String, Object>> countSysUserInfo();

    /**
     * 查询村干部
     * @return
     */
    @Select("SELECT dept.`name` as name,count(su.id) as num FROM sz_user su LEFT JOIN sys_dept dept ON dept.dept_id=su.did WHERE su.login=1 AND su.del_flag=0 AND role=2 GROUP BY dept.`name`")
    public List<Map<String, Object>> countCadresInfo();

    @Select("SELECT dept.name as name,count(sr.id) as num FROM sz_task_record sr LEFT JOIN sz_user su on su.id=sr.uid LEFT JOIN sys_dept dept on dept.dept_id=su.did GROUP BY dept.`name`")
    public List<Map<String, Object>> countTaskInfo();


    @Select("SELECT state,count(sr.id) as num FROM sz_task_record sr WHERE sr.create_time BETWEEN #{startTime} and #{endTime} GROUP BY state")
    public List<Map<String, Object>> countTaskInfoByCreateTime(@Param("startTime") String startTime,@Param("endTime") String endTime);


    @Select("SELECT state,count(sr.id) as num FROM sz_task_record sr WHERE sr.sh_time BETWEEN #{startTime} and #{endTime} GROUP BY state")
    public List<Map<String, Object>> countTaskInfoByExaimTime(@Param("startTime") String startTime,@Param("endTime") String endTime);


    @Select("SELECT dept.`name` as name,count(sr.id) as num FROM sz_task_record sr LEFT JOIN sz_user su on su.id=sr.uid LEFT JOIN sys_dept dept on dept.dept_id=su.did WHERE sr.sh_time GROUP BY dept.dept_id")
    public List<Map<String, Object>> countTaskInfoByVillage();

}
