package com.zzx.mq;


import com.zzx.config.RabbitMqConfig;
import com.zzx.dao.mongo.ControllerLogDAO;
import com.zzx.model.entity.ControllerLog;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author mqpearh
 */
@Component
@RabbitListener(queues = RabbitMqConfig.CONTROLLER_LOG_QUEUE)
public class ControllerLogListener {

    @Autowired
    ControllerLogDAO controllerLogDAO;

    @RabbitHandler
    public void save(ControllerLog log) {
        controllerLogDAO.saveControllerLog(log);
    }
}
