package com.example.demo.service.impl;

import com.example.demo.entity.DemoDO;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.el.BeanNameResolver;
import java.util.ArrayList;
import java.util.List;

/**
 * DemoServiceImpl
 *
 * @author mc
 * @date 2022年12月19日
 */
@Component
public class DemoServiceImpl implements DemoService {

    private static List<DemoDO> list = new ArrayList<>();

    static {
        for (long i = 0; i < 10; i++) {
            list.add(new DemoDO().setId(i).setAgentCode(i + "_code").setSecret(i + "_secret"));
        }
    }

    @Override
    @CacheEvict(cacheNames = "agentList", key = "'allAgents'")
    public List<DemoDO> add(DemoDO req) {
        // 将新增的用户添加到缓存的用户数据列表中
        list.add(req);
        return list;
    }

    @Override
    @CacheEvict(cacheNames = "agentList", key = "'allAgents'")
    public void update(DemoDO req) {
        List<DemoDO> newList = new ArrayList<>(list);
        for (DemoDO demoDO : newList) {
            if (req.getId().equals(demoDO.getId())) {
                demoDO.setSecret(req.getSecret());
                demoDO.setAgentCode(req.getAgentCode());
            }
        }
        list = newList;
    }

    @Override
    @CacheEvict(cacheNames = "agentList", key = "'allAgents'", allEntries = true)
    public void delete(Long id) {
        DemoDO demoDO = list.stream().filter(it -> it.getId().equals(id)).findFirst().orElse(null);
        list.remove(demoDO);
    }

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public DemoDO get(Long id) {
        DemoServiceImpl bean = applicationContext.getBean(DemoServiceImpl.class);
        Class<?> beanType = bean.getClass();
        if (Proxy.isProxyClass(beanType)) {
            // 说明userService是一个代理类
            System.out.printf("asdadsadsadsasdasd");
            // 可以使用Proxy.getInvocationHandler(beanType)获取到InvocationHandler等信息
        }

        return null;
    }


    @Override
    @Cacheable(value = "agentList", key = "'allAgents'")
    public List<DemoDO> list() {
        return list;
    }
}
