package com.zzx.model.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * NewestLogVO
 *
 * @author zzx
 * @date 2021.5.29 22:30
 */
@Data
public class NewestLogVO {
    private Integer type;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date left;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date right;
    private Integer size;
}
