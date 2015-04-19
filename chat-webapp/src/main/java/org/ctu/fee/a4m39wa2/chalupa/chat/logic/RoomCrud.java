package org.ctu.fee.a4m39wa2.chalupa.chat.logic;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.EqualParam;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.Param;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.RoomDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.exceptions.UniqueConstraintViolationException;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;

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
public class RoomCrud {

    @Inject
    private RoomDao dao;

    public Room find(long id) {
        return dao.find(id);
    }

    public List<Room> findAll(SelectionContext selectionContext) {

        List<Param<Room>> where = new ArrayList<>(selectionContext.getWhere().size());
        selectionContext.getWhere().forEach(w -> where.add(new EqualParam<>(w.getFirst(), w.getSecond())));

        int count = dao.countAll(where);
        selectionContext.setTotal(count);
        if (count == 0) {
            return Collections.emptyList();
        }

        return dao.findAll(
                selectionContext.getOffset(),
                selectionContext.getLimit(),
                selectionContext.getOrderBy(),
                where
        );
    }

    public Room create(String name) throws UniqueConstraintViolationException {
        if (!isUnique(name)) {
            throw new UniqueConstraintViolationException("name");
        }

        Room room = new Room();
        room.setName(name);

        dao.persist(room);
        return room;
    }

    public void changeName(Room room, String newName) throws UniqueConstraintViolationException {
        Room namedRoom = dao.findByName(newName);
        if (namedRoom == null || room.equals(namedRoom)) {
            room.setName(newName);
            dao.update(room);
        } else {
            throw new UniqueConstraintViolationException("name");
        }
    }

    public void remove(long id) {
        dao.remove(id);
    }

    private boolean isUnique(String name) {
        return dao.findByName(name) == null;
    }
}
