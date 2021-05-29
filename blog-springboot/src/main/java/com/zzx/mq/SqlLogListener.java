package com.zzx.mq;


import com.zzx.config.RabbitMqConfig;
import com.zzx.dao.mongo.SqlLogDAO;
import com.zzx.model.entity.SqlLog;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mqpearh
 */
@Component
@RabbitListener(queues = RabbitMqConfig.SQL_LOG_QUEUE)
public class SqlLogListener {

    @Autowired
    SqlLogDAO sqlLogDAO;

    @RabbitHandler
    public void log(SqlLog log) {
        sqlLogDAO.saveSqlLog(log);
    }
}
