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


import java.util.Collection;
import java.util.List;

/** Defines metadata about a platform component */
public interface ComponentDescriptor {

    /** @return the unique name of the component within the platform */
    String getName();

    /** @return the inputs to the component, e.g. Kafka topics it consumes. */
    default Collection<ComponentInput> getInputs() {
        return List.of();
    }

    /** @return the internals to the component, e.g. changelog or repartition Kafka topics. */
    default Collection<ComponentInternal> getInternals() {
        return List.of();
    }

    /** @return the outputs from the component, e.g. the Kafka topics it outputs too */
    default Collection<ComponentOutput> getOutputs() {
        return List.of();
    }
}
