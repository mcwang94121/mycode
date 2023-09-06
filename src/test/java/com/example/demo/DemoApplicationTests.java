package com.example.demo;

import com.example.demo.entity.DemoDO;
import com.example.demo.service.DemoService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private DemoService service;

    @Test
    void contextLoads() {

        DemoDO demoDO = new DemoDO();
        demoDO.setAgentCode("111");
        demoDO.setSecret("222");
        service.add(demoDO);

    }

    @Test
    void threadStopTest() {
        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (Thread.interrupted()) {
                        throw new RuntimeException();
                    }
                    Thread.sleep(1);
                    System.out.println("执行" + i);
                }
            }
        };
        runnable.run();
        new Thread(runnable).interrupt();
    }

}
