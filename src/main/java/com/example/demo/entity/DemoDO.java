package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: JamesZY
 * @date: 2021/6/4
 * @description:
 */
@Data
@Accessors(chain = true)
@TableName("WWYT_BASE_AGENT")
public class DemoDO implements Serializable {

    private static final long serialVersionUID = -5207157957900602605L;

    /**
     * ID
     */
    private Long id;


    private String agentCode;

    /**
     * 密码
     */
    private String secret;

}
