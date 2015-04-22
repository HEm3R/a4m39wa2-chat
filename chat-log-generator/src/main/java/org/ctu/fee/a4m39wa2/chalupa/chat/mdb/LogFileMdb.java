package org.ctu.fee.a4m39wa2.chalupa.chat.mdb;

import org.jboss.ejb3.annotation.Pool;
import org.jboss.ejb3.annotation.ResourceAdapter;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ResourceAdapter("activemq-ra")
@MessageDriven(
        name = "MessageMDBSample",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "chatQueue"),
                @ActivationConfigProperty(propertyName = "ConnectionFactory", propertyValue = "chat-log-generator-factory"),
                @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
        }
)
@Pool("log-file-generator-mdb-pool")
@TransactionManagement(TransactionManagementType.BEAN) // allow auto acknowledge
public class LogFileMdb implements MessageListener {

    @Inject
    private LogFileGenerator generator;

    @Override
    public void onMessage(javax.jms.Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                if (objectMessage.getObject() instanceof Room) {
                    Room room = (Room) objectMessage.getObject();
                    log.info("Received room message: {}", room.getId());

                    generator.generate(room); // async, mdb should not process time-consuming task;

                    log.info("Log generation completed");
                }
            } catch (JMSException e) {
                log.error("Error by JMS consumption", e);
            }
        }
    }
}
