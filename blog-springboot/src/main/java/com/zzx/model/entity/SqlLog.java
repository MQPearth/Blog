package com.zzx.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * SqlLog
 *
 * @author zzx
 * @date 2021.5.29 17:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "sql_log")
public class SqlLog extends BaseLog {
    private String sql;
}
