package org.ctu.fee.a4m39wa2.chalupa.chat.logic;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class RoomLogGenerator {

    @Resource(mappedName = "java:/activemq/chatQueue")
    private Destination destination;

    @Resource(mappedName = "java:/activemq/chatWebappXA")
    private QueueConnectionFactory cf;

    @Asynchronous
    public void requestLogGeneration(Room room) {
        try (Connection connection = cf.createConnection()) {
            connection.start();

            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final MessageProducer producer = session.createProducer(destination);
            final ObjectMessage message = session.createObjectMessage();

            message.setObject(room);
            producer.send(message);
            session.close();
        } catch (Exception e) {
            log.error("Exception thrown by JMS sending", e);
        }
    }
}
