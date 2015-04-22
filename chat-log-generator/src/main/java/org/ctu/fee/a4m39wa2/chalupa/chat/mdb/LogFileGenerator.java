package org.ctu.fee.a4m39wa2.chalupa.chat.mdb;

import org.ctu.fee.a4m39wa2.chalupa.chat.dao.EqualParam;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.MessageDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Message;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;

import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class LogFileGenerator {

    @Inject
    private MessageDao messageDao;

    @Asynchronous
    public void generate(Room room) {
        List<Message> messages = getMessages(room);
        log.info("LoadMessages to write: {}", messages.size());
        writeMessages(room, messages);

        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(5));
        } catch (InterruptedException e) {
            log.error("Could not simulate time-consuming processing", e);
        }

        log.info("Log generation completed");
    }

    // We need to fetch users for messages, do not use room.getMessages directly
    private List<Message> getMessages(Room room) {

        return messageDao.findAll(
                null,
                null,
                null,
                Arrays.asList(new EqualParam<>("room", room.getId().toString())),
                (root -> root.fetch("user"))
        );
    }

    private void writeMessages(Room room, List<Message> messages) {
        try {
            File temp = File.createTempFile("room-" + room.getId() + "-" + new Date().getTime(), ".log");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
                for (Message m : messages) {
                    bw.write(m.getUser().getUsername() + "::" + m.getText() + "\n");
                }
            }
        } catch (IOException e) {
            log.error("Could not create log file with messages of room", e);
        }
    }
}
