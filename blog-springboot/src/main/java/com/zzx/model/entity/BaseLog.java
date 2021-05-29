package com.zzx.model.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;

/**
 * BaseLog
 *
 * @author zzx
 * @date 2021.5.29 15:57
 */
@Data
public class BaseLog implements Serializable {

    @MongoId
    private String id;

    /**
     * 执行时间
     */
    private Integer time;

    private Date date;
}
