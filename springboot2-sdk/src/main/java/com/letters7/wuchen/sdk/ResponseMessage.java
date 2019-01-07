package com.letters7.wuchen.sdk;

import com.letters7.wuchen.sdk.model.BaseBo;
import com.letters7.wuchen.springboot2.utils.exception.ExceptionHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author wuchenl
 *  2018/12/28.
 *  通用返回类
 */
@Getter
public class ResponseMessage implements BaseBo {
    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳",example = "1546052704190")
    private Long timestamp;
    /**
     * 成功状态
     */
    @ApiModelProperty(value = "是否成功",example = "true")
    private Boolean success;
    /**
     * 状态码
     */
    @ApiModelProperty(value = "响应状态码",example = "200")
    private Integer code;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "异常信息",example = "请求失败")
    private String message;
    /**
     * 数据存放字段
     */
    @ApiModelProperty(value = "数据信息",example = "{\"name\":\"test\"}")
    private Object data;

    //成功构造
    public static ResponseMessage ok() {
        return ok(null);
    }


    public static ResponseMessage ok(Object data) {
        ResponseMessage msg = new ResponseMessage();
        msg.timeStamp().code(200).data(data).success(Boolean.TRUE);
        return msg;
    }


    //失败构造
    public static ResponseMessage error(Exception ex) {
        return error(500, ExceptionHelper.getBootMessage(ex));
    }

    public static ResponseMessage error(String message) {
        return error(500, message);
    }


    public static ResponseMessage error(int code, String message) {
        ResponseMessage msg = new ResponseMessage();
        msg.code(code).message(message).timeStamp().success(Boolean.FALSE);
        return msg;
    }


    //设置报文头信息
    private ResponseMessage timeStamp() {
        this.timeStamp(System.currentTimeMillis());
        return this;
    }

    private ResponseMessage timeStamp(Long timeStamp) {
        this.timestamp = timeStamp;
        return this;
    }

    public ResponseMessage success(Boolean success){
        this.success=success;
        return this;
    }

    public ResponseMessage code(int code) {
        this.code = code;
        return this;
    }

    public ResponseMessage message(String message) {
        this.message = message;
        return this;
    }


    //设置数据信息
    public <T> ResponseMessage data(T data) {
        //处理分页信息
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this , ToStringStyle.SHORT_PREFIX_STYLE );
    }

}
