package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.BaseEntity;

import java.util.List;

public final class DaoUtils {

    public static <T extends BaseEntity> T getSingleResult(List<T> entities) {
        if (entities.isEmpty()) {
            return null;
        }
        if (entities.size() > 1) {
            throw new RuntimeException("Cannot retrieve single result");
        }
        return entities.get(0);
    }

    private DaoUtils() {}
}
