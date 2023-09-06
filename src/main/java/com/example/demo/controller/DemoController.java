package com.example.demo.controller;

import com.example.demo.entity.DemoDO;
import com.example.demo.service.DemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * demoCollector
 *
 * @author mc
 * @date 2022年12月19日
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping()
public class DemoController {

    private final DemoService service;

    @PostMapping("/add")
    public void add(@RequestBody DemoDO req) {
        service.add(req);
    }

    @PostMapping("/update")
    public void update(@RequestBody DemoDO req) {
        service.update(req);
    }

    @DeleteMapping("/del/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @GetMapping("/one")
    public DemoDO get(Long id) {
        return service.get(id);
    }

    @GetMapping("/list")
    public List<DemoDO> list() {
        return service.list();
    }

}
