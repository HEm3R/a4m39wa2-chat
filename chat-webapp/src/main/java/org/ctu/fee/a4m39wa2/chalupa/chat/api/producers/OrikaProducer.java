package org.ctu.fee.a4m39wa2.chalupa.chat.api.producers;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.dto.error.MessageDto;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@ApplicationScoped
public class OrikaProducer {

    private MapperFactory mapperFactory;
    private MapperFacade mapperFacade;

    public OrikaProducer() {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        // register custom conversions and mappings
        mapperFacade = mapperFactory.getMapperFacade();

        mapperFactory
                .registerClassMap(mapperFactory.classMap(Message.class, MessageDto.class)
                .field("user.username", "author")
                .field("user.id", "authorId")
                .byDefault().toClassMap()
        );
    }

    @Produces
    public MapperFacade getMapperFacade() {
        return mapperFacade;
    }
}
