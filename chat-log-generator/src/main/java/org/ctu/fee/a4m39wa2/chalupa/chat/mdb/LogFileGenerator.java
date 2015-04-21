package org.ctu.fee.a4m39wa2.chalupa.chat.mdb;

import org.jboss.ejb3.annotation.Pool;
import org.jboss.ejb3.annotation.ResourceAdapter;

import org.ctu.fee.a4m39wa2.chalupa.chat.dao.EqualParam;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.MessageDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Message;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ResourceAdapter("activemq-ra")
@MessageDriven(
        name = "MessageMDBSample",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/activemq/chatQueue"),
                @ActivationConfigProperty(propertyName = "ConnectionFactory", propertyValue = "chat-log-generator-factory")
        }
)
@Pool("log-file-generator-mdb-pool")
public class LogFileGenerator implements MessageListener {

    @Inject
    private MessageDao messageDao;

    @Override
    public void onMessage(javax.jms.Message message) {
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                if (objectMessage.getObject() instanceof Room) {
                    Room room = (Room) objectMessage.getObject();
                    List<Message> messages = messageDao.findAll(
                            null, null, null, Arrays.asList(new EqualParam<>("room", room.getId().toString())), (root -> root.fetch("user"))
                    );
                    log.info("KUAAAA {}", messages);
                }
            } catch (JMSException e) {
                log.error("", e);
            }
        }
    }
}
