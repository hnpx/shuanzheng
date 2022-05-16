package com.px.pa.modulars.home.service;

import com.px.pa.modulars.home.mapper.HomeInfoMapper;
import com.px.pa.vo.result.HomeDataInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouz
 */
@Service
@Transactional
public class HomeInfoService {
    @Autowired
    private HomeInfoMapper homeInfoMapper;

    /**
     * 读取首页统计信息
     *
     * @return
     */
    public HomeDataInfoResult readHome() {
        HomeDataInfoResult result = new HomeDataInfoResult();
        List<Map<String, Object>> peopleInfos = this.homeInfoMapper.countPeopleInfo();
        List<Map<String, Object>> sysUserInfos = this.homeInfoMapper.countSysUserInfo();
        List<Map<String, Object>> cadresInfos = this.homeInfoMapper.countCadresInfo();
//        Map<String, Integer> taskInfo = this.homeInfoMapper.countTaskInfo();
        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> taskTodayInfos = this.homeInfoMapper.countTaskInfoByCreateTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00",
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59");
        List<Map<String, Object>> taskTodayShInfos = this.homeInfoMapper.countTaskInfoByExaimTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00",
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59");
        now.minusDays(1);
        List<Map<String, Object>> taskYesterdayInfos = this.homeInfoMapper.countTaskInfoByCreateTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00",
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59");

        Map<String, Integer> peopleInfo=this.packInfoToMap(peopleInfos,"name","num");
        Map<String, Integer> sysUserInfo=this.packInfoToMap(sysUserInfos,"name","num");
        Map<String, Integer> cadresInfo=this.packInfoToMap(cadresInfos,"name","num");
        Map<String, Integer> taskTodayInfo=this.packInfoToMap(taskTodayInfos,"state","num");
        Map<String, Integer> taskYesterdayInfo=this.packInfoToMap(taskYesterdayInfos,"state","num");
        Map<String, Integer> taskTodayShInfo=this.packInfoToMap(taskTodayShInfos,"state","num");

        int peopleTotal = this.countValue(peopleInfo);
        int sysUserTotal = this.countValue(sysUserInfo);
        int cadrsTotal = this.countValue(cadresInfo);


        result.setPeopleInfoMap(peopleInfo);
        result.setPeopleNum(peopleTotal);

        result.setSysUserMap(sysUserInfo);
        result.setSysUserNum(sysUserTotal);

        result.setVillagerNum(peopleInfo.size());
        result.setCadresNum(cadrsTotal);

        result.setTodayFinishTaskNum(this.countValue(taskTodayInfo));
        result.setYesterdayFinishTaskNum(this.countValue(taskYesterdayInfo));
        result.setTodayExamineTaskNum(this.countValue(taskTodayShInfo));

        List<Map<String,Object>> taskInfoByVillages=this.homeInfoMapper.countTaskInfoByVillage();
        Map<String, Integer> taskInfoByVillage=this.packInfoToMap(taskInfoByVillages,"name","num");
        result.setTaskFinishMap(taskInfoByVillage);
        return result;
    }

    /**
     * 包装数据
     * @param maps
     * @param key
     * @param numKey
     * @return
     */
    private Map<String,Integer> packInfoToMap(List<Map<String, Object>> maps,String  key,String numKey){
        Map<String,Integer> info=new HashMap<>();
        for(Map<String,Object> item:maps){
            if(item.containsKey(key)&&item.containsKey(numKey)){
                info.put(item.get(key).toString(),item.get(numKey)==null?0:Integer.parseInt(item.get(numKey).toString()));
            }
        }
        return info;
    }

    private Integer countValue(Map<String, Integer> info) {
        int total = 0;
        for (Map.Entry<String, Integer> me : info.entrySet()) {
            Integer v = me.getValue();
            if (v != null) {
                total = total + v;
            }
        }
        return total;
    }
}
