package com.zzx.model.entity;


import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 分页结果类
 *
 * @param<T>
 */
@Data
@ToString
public class PageResult<T> {
    private Long total; //数据条数
    private List<T> rows; //数据

    public PageResult(Long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

}
