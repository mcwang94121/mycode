package com.example.demo.service;

import com.example.demo.entity.DemoDO;

import java.util.List;

/**
 * DemoService
 *
 * @author mc
 * @date 2022年12月19日
 */
public interface DemoService {

    List<DemoDO> add(DemoDO req);

    void update(DemoDO req);

    List<DemoDO> list();

    void delete(Long id);

    DemoDO get(Long id);
}
