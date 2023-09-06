package com.example.demo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.entity.DemoDO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * DemoServiceImpl
 *
 * @author mc
 * @date 2022年12月19日
 */
@Component
public class DemoServiceImpl1 {

    private static List<DemoDO> list = new ArrayList<>();

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    static {
        for (long i = 0; i < 10; i++) {
            list.add(new DemoDO().setId(i).setAgentCode(i + "_code").setSecret(i + "_secret"));
        }
    }

    public List<DemoDO> add(DemoDO req) {
        // 将新增的用户添加到缓存的用户数据列表中 模拟数据库操作
        list.add(req);

        // 手动更新缓存
        String allAgents = redisTemplate.opsForValue().get("allAgents");
        if (StrUtil.isNotBlank(allAgents)) {
            List<DemoDO> demoDOList = JSONArray.parseArray(allAgents, DemoDO.class);
            if (CollUtil.isEmpty(demoDOList)) {
                demoDOList = new ArrayList<>();
            }
            demoDOList.add(req);
            redisTemplate.opsForValue().set("allAgents", JSON.toJSONString(demoDOList));
        }
        return list;
    }


    public void update(DemoDO req) {
        List<DemoDO> newList = new ArrayList<>(list);
        for (DemoDO demoDO : newList) {
            if (req.getId().equals(demoDO.getId())) {
                demoDO.setSecret(req.getSecret());
                demoDO.setAgentCode(req.getAgentCode());
            }
        }

        // 手动更新缓存
        redisTemplate.delete("allAgents");
        list = newList;
    }


    public void delete(Long id) {
        DemoDO demoDO = list.stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null);
        list.remove(demoDO);
        // 手动更新缓存
        redisTemplate.delete("allAgents");
    }


    public DemoDO get(Long id) {
        // 手动更新缓存
        String allAgents = redisTemplate.opsForValue().get("allAgents");
        if (StrUtil.isNotBlank(allAgents)) {
            List<DemoDO> demoDOList = JSONArray.parseArray(allAgents, DemoDO.class);
            if (CollUtil.isNotEmpty(demoDOList)) {
                return demoDOList.stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null);
            }
        }
        redisTemplate.opsForValue().set("allAgents", JSON.toJSONString(list));
        return list.stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null);
    }


    public List<DemoDO> list() {
        // 手动更新缓存
        String allAgents = redisTemplate.opsForValue().get("allAgents");
        if (StrUtil.isNotBlank(allAgents)) {
            List<DemoDO> demoDOList = JSONArray.parseArray(allAgents, DemoDO.class);
            if (CollUtil.isNotEmpty(demoDOList)) {
                return demoDOList;
            }
        }
        redisTemplate.opsForValue().set("allAgents", JSON.toJSONString(list));
        return list;
    }
}
