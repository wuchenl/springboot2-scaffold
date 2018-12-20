package com.letters7.wuchen.demo.redis.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author wuchenl
 * @date 2018/12/20.
 */
@Data
@Builder
public class User {
    private String userName;

    private String sex;

    private String address;
}
