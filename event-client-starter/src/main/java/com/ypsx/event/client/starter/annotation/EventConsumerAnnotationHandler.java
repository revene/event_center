package com.ypsx.event.client.starter.annotation;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * @author chuchengyi
 */
@ConditionalOnMissingBean
public class EventConsumerAnnotationHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {

    }
}
