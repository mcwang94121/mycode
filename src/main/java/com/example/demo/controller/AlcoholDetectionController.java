package com.example.demo.controller;

import com.example.demo.service.AlcoholDetectionService;
import com.example.demo.tcpTest.netty1.netty.codec.dto.AlcoholPushDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * AlcoholDetectionController
 *
 * @author mc
 * @date 2023年09月05日
 */
@Slf4j
@RestController
@RequestMapping("/alcohol")
public class AlcoholDetectionController {

    @Resource
    private AlcoholDetectionService service;

    /**
     * xxx修改
     */
    @PutMapping
    public void update(@RequestBody AlcoholPushDTO cmd) throws InterruptedException {
        service.push(cmd);
    }

}
