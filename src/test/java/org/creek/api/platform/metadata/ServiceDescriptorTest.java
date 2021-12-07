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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ServiceDescriptorTest {

    private final ServiceDescriptor descriptor = new TestServiceDescriptor();

    @Test
    void shouldReturnStandardAggregateName() {
        assertThat(new TestServiceDescriptor().getName(), is("test"));
        assertThat(new SupportedDescriptor().getName(), is("supported"));
    }

    @Test
    void shouldThrowOnNonStandardAggregateClassName() {
        // Given:
        final ServiceDescriptor descriptor = new NonStandard();

        // When:
        final Exception e = assertThrows(UnsupportedOperationException.class, descriptor::getName);

        // Then:
        assertThat(
                e.getMessage(),
                is("Non-standard class name: either override getName or use standard naming"));
    }

    @Test
    void shouldDefaultImageNameToServiceName() {
        assertThat(descriptor.getDockerImage(), is(descriptor.getName()));
    }

    @Test
    void shouldDefaultToNoTestEnv() {
        assertThat(descriptor.getTestEnvironment().entrySet(), is(empty()));
    }

    private static final class TestServiceDescriptor implements ServiceDescriptor {}

    private static final class SupportedDescriptor implements AggregateDescriptor {}

    private static final class NonStandard implements ServiceDescriptor {}
}
