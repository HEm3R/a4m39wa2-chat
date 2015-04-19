package org.ctu.fee.a4m39wa2.chalupa.chat.logic;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.EqualParam;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.MessageDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.Param;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Message;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.SecurityContext;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MessageCrud {

    @Inject
    private MessageDao dao;

    @Inject
    private SecurityContext securityContext;

    public Message find(long id) {
        return dao.find(id);
    }

    public List<Message> findAllByRoom(Room room, SelectionContext selectionContext) {

        List<Param<Message>> where = new ArrayList<>(selectionContext.getWhere().size());
        selectionContext.getWhere().forEach(w -> where.add(new EqualParam<>(w.getFirst(), w.getSecond())));
        where.add(new EqualParam<>("room", room.getId().toString())); // select by room

        int count = dao.countAll(where);
        selectionContext.setTotal(count);
        if (count == 0) {
            return Collections.emptyList();
        }

        return dao.findAll(
                selectionContext.getOffset(),
                selectionContext.getLimit(),
                selectionContext.getOrderBy(),
                where,
                (root -> root.fetch("user"))
        );
    }

    public Message create(Room room, String text) {
        Message message = new Message();
        message.setUser(securityContext.getUser());
        message.setRoom(room);
        message.setText(text);

        dao.persist(message);
        return message;
    }

    public void remove(long id) {
        dao.remove(id);
    }
}
