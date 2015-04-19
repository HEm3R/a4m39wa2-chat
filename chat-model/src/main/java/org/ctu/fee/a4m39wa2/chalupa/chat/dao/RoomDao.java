package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.Room;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class RoomDao extends AbstractDao<Room> {

    public Room findByName(String name) {
        return findByParams(new EqualParam<>("name", name));
    }
}
