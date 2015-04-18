package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.selectable;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class SelectionFeature implements DynamicFeature {

    @Inject
    private Instance<SelectionRequestFilter> requestFilterInstance;

    /**
     * Creates all {@link SelectionRequestFilter} own local information of {@link Selectable} annotation.
     */
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        final Selectable selectable = resourceInfo.getResourceMethod().getAnnotation(Selectable.class);
        if (selectable == null) {
            return;
        }

        final SelectionRequestFilter requestFilter = requestFilterInstance.get();
        requestFilter.init(selectable);
        featureContext.register(requestFilter);
    }
}

