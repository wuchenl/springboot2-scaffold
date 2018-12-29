package com.letters7.wuchen.web.exception;

import com.letters7.wuchen.sdk.ResponseMessage;
import com.letters7.wuchen.sdk.exception.BusinessException;
import com.letters7.wuchen.springboot2.utils.exception.UtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/29 10:06
 * @desc 针对RestController 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, ResponseBody.class})
public class GlobalRestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

    /**
     * 处理所有异常，并按照统一返回格式返回。不改变HTTP STATUS码
     *
     * @param exception 异常
     * @return 转换后的格式信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseMessage handleException(Exception exception) {
        Integer errCode = 500;
        log.error("出现异常:{}", UtilException.getBootMessage(exception));
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            errCode = businessException.getStatus();
        } else {
            errCode = 500;
        }
        ResponseMessage responseMessage = ResponseMessage.error(errCode, UtilException.getBootMessage(exception));

        return responseMessage;
    }
}
