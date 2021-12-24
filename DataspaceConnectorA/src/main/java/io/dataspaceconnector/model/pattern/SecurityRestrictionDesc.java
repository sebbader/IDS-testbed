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
package io.dataspaceconnector.model.pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class for inputs of a policy pattern that describes the restriction of data usage to a
 * specific connector.
 */
@Schema(example = "{\n"
        + "\t\"type\": \"SECURITY_PROFILE_RESTRICTED_USAGE\",\n"
        + "\t\"profile\": \"BASE_SECURITY_PROFILE\"\n"
        + "}", description = "Possible profiles are: idsc:BASE_SECURITY_PROFILE, "
        + "idsc:TRUST_SECURITY_PROFILE and idsc:TRUST_PLUS_SECURITY_PROFILE")
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityRestrictionDesc extends PatternDesc {

    /**
     * The security profile.
     */
    @JsonProperty("profile")
    private String profile;
}
