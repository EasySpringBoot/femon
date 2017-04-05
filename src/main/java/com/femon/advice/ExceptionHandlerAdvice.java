package com.femon.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统异常全局统一处理
 *
 * Created by jack on 2017/4/5.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(value = Exception.class) //表示捕捉到所有的异常，你也可以捕捉一个你自定义的异常
    public ModelAndView exception(Exception exception, WebRequest request) {
        //error页面
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", exception.getMessage());
        modelAndView.addObject("stackTrace", exception.getStackTrace());
        return modelAndView;
    }
}
