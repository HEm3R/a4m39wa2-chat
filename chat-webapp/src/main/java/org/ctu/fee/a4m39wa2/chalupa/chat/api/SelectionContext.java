package org.ctu.fee.a4m39wa2.chalupa.chat.api;

import org.ctu.fee.a4m39wa2.chalupa.chat.dao.OrderDirection;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.TwoValueObject;

import javax.enterprise.context.RequestScoped;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@RequestScoped
public class SelectionContext implements Serializable {

    private static final long serialVersionUID = 1;

    // request meta-data
    @Getter @Setter private List<TwoValueObject<String, String>> where; // where eq

    @Getter @Setter private List<TwoValueObject<String, OrderDirection>> orderBy;
    @Getter @Setter private int offset;
    @Getter @Setter private int limit;

    // response meta-data
    @Getter @Setter private Integer total;
}
