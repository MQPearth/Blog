package com.zzx.service;

import com.zzx.dao.mongo.ControllerLogDAO;
import com.zzx.dao.mongo.SqlLogDAO;
import com.zzx.model.entity.ControllerLog;
import com.zzx.model.entity.SqlLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * LogService
 *
 * @author zzx
 * @date 2021.5.29 17:57
 */
@Service
public class LogService {

    @Autowired
    ControllerLogDAO controllerLogDAO;

    @Autowired
    SqlLogDAO sqlLogDAO;

    public List<ControllerLog> findControllerNewestLog(Date left, Date right, Integer size) {
        return controllerLogDAO.findNewestLog(left, right, size);
    }

    public List<SqlLog> findSqlNewestLog(Date left, Date right, Integer size) {
        return sqlLogDAO.findNewestLog(left, right, size);
    }
}
