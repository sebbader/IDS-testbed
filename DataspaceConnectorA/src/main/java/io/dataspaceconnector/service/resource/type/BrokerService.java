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
package io.dataspaceconnector.service.resource.type;

import io.dataspaceconnector.model.base.RegistrationStatus;
import io.dataspaceconnector.model.broker.Broker;
import io.dataspaceconnector.model.broker.BrokerDesc;
import io.dataspaceconnector.repository.BrokerRepository;
import io.dataspaceconnector.service.resource.base.BaseEntityService;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for brokers.
 */
@Service("ConfigurationBrokerService") // Clashes with IDS-Messaging-Services brokerService Bean
@NoArgsConstructor
@Transactional
public class BrokerService extends BaseEntityService<Broker, BrokerDesc> {

    /**
     * Finds a broker by the uri.
     *
     * @param location The uri of the broker.
     * @return The uuid of the broker.
     */
    public Optional<UUID> findByLocation(final URI location) {
        return ((BrokerRepository) getRepository()).findByLocation(location);
    }

    /**
     * This method updates the registration status of the broker.
     *
     * @param location The uri of the broker.
     * @param status   The registration status
     */
    public void setRegistrationStatus(final URI location, final RegistrationStatus status) {
        ((BrokerRepository) getRepository()).setRegistrationStatus(location, status);
    }
}
