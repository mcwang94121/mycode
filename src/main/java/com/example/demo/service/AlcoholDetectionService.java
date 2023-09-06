package com.example.demo.service;

import com.example.demo.tcpTest.netty1.netty.codec.dto.AlcoholPushDTO;

/**
 * alcoholDetectionService
 *
 * @author mc
 * @date 2023年09月05日
 */
public interface AlcoholDetectionService {
    void push(AlcoholPushDTO cmd) throws InterruptedException;
}
