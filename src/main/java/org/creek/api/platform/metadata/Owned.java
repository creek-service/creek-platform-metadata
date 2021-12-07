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

/**
 * A marker interface to indicate that a resource is conceptually owned by a component.
 *
 * <p>An owned resource, e.g. a Kafka topic, is a resource that an aggregate or service is
 * responsible for creating/managing and conceptually owns. Owned resources should be created as
 * part of the deployment process of the component, either directly by a service or via additional
 * tooling.
 *
 * <p>For example, normally an output topic from a service will be owned by that service. Other
 * services wishing to consume the topic will create non-owned input topic descriptors. However,
 * there are times when an input topic may be owned, for example a service may accept requests from
 * any other service via an owned input topic. Other services wishing to send a request will create
 * non-owned output topic descriptors.
 */
public interface Owned {}
