package com.letters7.wuchen.demo.redis.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wuchenl
 * @date 2018/12/20.
 */
@Data
@Builder
public class User {
    @NotNull(message="用户名不得为空！")
    private String userName;

    private String sex;

    private String address;
}
