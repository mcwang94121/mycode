package com.example.demo.config;

import com.joysuch.open.platform.common.exception.BusinessException;
import com.joysuch.open.platform.common.pojo.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础异常处理器
 *
 * @author mjshine
 * @date 2022年02月24日
 */
@Slf4j
public class BaseExceptionHandler {

    /*========== 自定义异常 Begin ==========*/

    /**
     * 处理业务异常
     *
     * @param request {@link HttpServletRequest}
     * @param ex      {@link BusinessException}
     * @return {@link RespVO}
     */
    @ExceptionHandler(RuntimeException.class)
    public RespVO<Void> handle(HttpServletRequest request, RuntimeException ex) {
        return RespVO.failure(500, ex.getMessage());
    }

}
