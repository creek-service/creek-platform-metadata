/*
 * Copyright 2021 Creek Contributors (https://github.com/creek-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.creek.api.platform.metadata;


import java.util.Map;

/** Type defining metadata data about a service. */
public interface ServiceDescriptor extends ComponentDescriptor {

    /**
     * The simple name of the docker image that contains the service.
     *
     * <p>Given a full image name of {@code confluentinc/cp-kafka:6.1.2}, this method should return
     * {@code cp-kafka}.
     *
     * @return the service's docker image name.
     */
    default String getDockerImage() {
        return getName();
    }

    /**
     * Allows customisation of the environment variables available to the service during system
     * testing.
     */
    default Map<String, String> getTestEnvironment() {
        return Map.of();
    }
}
