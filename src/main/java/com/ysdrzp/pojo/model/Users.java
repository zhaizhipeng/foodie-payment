package com.ysdrzp.pojo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Users {

    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 由我分配给用户的密码，这个密码存入数据库需要加密
     */
    private String password;

    /**
     * 用户访问有效期，20元/月
     */
    @Column(name = "end_date")
    private Date endDate;
}