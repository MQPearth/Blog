package com.zzx.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ControllerLog
 *
 * @author zzx
 * @date 2021.5.29 15:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "controller_log")
public class ControllerLog extends BaseLog {
    private String url;
    private String requestMethod;
    private String methodName;
    private String args;
    private String returnValue;
}
