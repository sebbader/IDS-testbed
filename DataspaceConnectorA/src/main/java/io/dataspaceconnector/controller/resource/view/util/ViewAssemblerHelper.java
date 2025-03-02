/*
 * Copyright 2020 Fraunhofer Institute for Software and Systems Engineering
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dataspaceconnector.controller.resource.view.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Helper for building self-links.
 */
@Component
public final class ViewAssemblerHelper {

    /**
     * The HTTP base URL of the application.
     */
    private static String baseUrl;

    /**
     * Setter for populating static attribute with property.
     *
     * @param applicationBaseUrl the base URL.
     */
    @Value("${application.http.base-url}")
    @SuppressFBWarnings(
            value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
            justification = "baseUrl needs to be populated using value annotation"
    )
    public void setBaseUrl(final String applicationBaseUrl) {
        ViewAssemblerHelper.baseUrl = applicationBaseUrl;
    }
    /**
     * Default constructor.
     */
    private ViewAssemblerHelper() {
        // Nothing to do here. Intentionally empty.
    }

    /**
     * Builds the complete self-link for an entity. As no request context is available to obtain
     * the application's base URL from when communicating via IDSCPv2, this method then
     * defaults to using the base URL set in application.properties.
     *
     * @param entityId The entity id.
     * @param tClass   The controller class for managing the entity class.
     * @param <T>      Type of the entity.
     * @return The self-link of the entity.
     * @throws IllegalArgumentException if the class is null.
     */
    public static <T> Link getSelfLink(final UUID entityId, final Class<T> tClass) {
        var link = linkTo(tClass).slash(entityId).withSelfRel();
        if (!link.toUri().isAbsolute()) {
            link = Link.of(baseUrl + link.getHref());
        }

        return link;
    }
}
