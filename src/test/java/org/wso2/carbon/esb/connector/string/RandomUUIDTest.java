/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.wso2.carbon.esb.connector.string;

import org.apache.synapse.MessageContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.esb.connector.string.utils.constants.Constant;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * Unit tests for {@link RandomUUID}.
 *
 * Issue #196: A debug {@code <log level="full">} mediator was accidentally left in
 * {@code string/randomUUID.xml}, causing a full SOAP-envelope INFO log on every
 * invocation. The log element was removed in v1.0.3. These tests verify the
 * connector's Java behaviour: UUID generation and variable assignment.
 */
@ExtendWith(MockitoExtension.class)
public class RandomUUIDTest {

    private static final Pattern UUID_V4_PATTERN =
            Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    @Mock
    private MessageContext messageContext;

    // -----------------------------------------------------------------------
    // Happy-path: explicit targetVariable
    // -----------------------------------------------------------------------

    @Test
    void test_connect_withExplicitTargetVariable_setsUUIDUnderThatVariable() throws Exception {

        RandomUUID connector = new RandomUUID();
        AtomicReference<String> capturedName = new AtomicReference<>();
        AtomicReference<String> capturedValue = new AtomicReference<>();

        try (MockedStatic<ConnectorUtils> connectorUtils = Mockito.mockStatic(ConnectorUtils.class)) {
            connectorUtils.when(() -> ConnectorUtils.lookupTemplateParamater(messageContext, Constant.TARGET))
                          .thenReturn("myUuidVar");

            Mockito.doAnswer(invocation -> {
                capturedName.set(invocation.getArgument(0));
                capturedValue.set(((Object) invocation.getArgument(1)).toString());
                return null;
            }).when(messageContext).setVariable(Mockito.anyString(), Mockito.any());

            connector.connect(messageContext);
        }

        Assertions.assertEquals("myUuidVar", capturedName.get(),
                "UUID should be stored under the caller-supplied targetVariable");
        Assertions.assertNotNull(capturedValue.get(), "UUID value must not be null");
        Assertions.assertTrue(UUID_V4_PATTERN.matcher(capturedValue.get()).matches(),
                "Stored value must be a valid UUID v4 string, got: " + capturedValue.get());
    }

    // -----------------------------------------------------------------------
    // Default variable: no targetVariable supplied → falls back to "uuid"
    // -----------------------------------------------------------------------

    @Test
    void test_connect_withNoTargetVariable_setsUUIDUnderDefaultName() throws Exception {

        RandomUUID connector = new RandomUUID();
        AtomicReference<String> capturedName = new AtomicReference<>();
        AtomicReference<String> capturedValue = new AtomicReference<>();

        try (MockedStatic<ConnectorUtils> connectorUtils = Mockito.mockStatic(ConnectorUtils.class)) {
            // Simulate the template parameter being absent (returns null)
            connectorUtils.when(() -> ConnectorUtils.lookupTemplateParamater(messageContext, Constant.TARGET))
                          .thenReturn(null);

            Mockito.doAnswer(invocation -> {
                capturedName.set(invocation.getArgument(0));
                capturedValue.set(((Object) invocation.getArgument(1)).toString());
                return null;
            }).when(messageContext).setVariable(Mockito.anyString(), Mockito.any());

            connector.connect(messageContext);
        }

        Assertions.assertEquals(Constant.SAVE_TO_PROPERTY_UUID, capturedName.get(),
                "UUID should be stored under the default '" + Constant.SAVE_TO_PROPERTY_UUID + "' variable");
        Assertions.assertNotNull(capturedValue.get(), "UUID value must not be null");
        Assertions.assertTrue(UUID_V4_PATTERN.matcher(capturedValue.get()).matches(),
                "Stored value must be a valid UUID v4 string, got: " + capturedValue.get());
    }

    // -----------------------------------------------------------------------
    // Default variable: blank targetVariable supplied → falls back to "uuid"
    // -----------------------------------------------------------------------

    @Test
    void test_connect_withBlankTargetVariable_setsUUIDUnderDefaultName() throws Exception {

        RandomUUID connector = new RandomUUID();
        AtomicReference<String> capturedName = new AtomicReference<>();

        try (MockedStatic<ConnectorUtils> connectorUtils = Mockito.mockStatic(ConnectorUtils.class)) {
            connectorUtils.when(() -> ConnectorUtils.lookupTemplateParamater(messageContext, Constant.TARGET))
                          .thenReturn("   ");   // blank — treated as absent by PropertyReader

            Mockito.doAnswer(invocation -> {
                capturedName.set(invocation.getArgument(0));
                return null;
            }).when(messageContext).setVariable(Mockito.anyString(), Mockito.any());

            connector.connect(messageContext);
        }

        Assertions.assertEquals(Constant.SAVE_TO_PROPERTY_UUID, capturedName.get(),
                "Blank targetVariable should fall back to the default 'uuid' variable");
    }

    // -----------------------------------------------------------------------
    // Uniqueness: two consecutive calls should produce different UUIDs
    // -----------------------------------------------------------------------

    @Test
    void test_connect_calledTwice_producesDifferentUUIDs() throws Exception {

        RandomUUID connector = new RandomUUID();
        AtomicReference<String> first = new AtomicReference<>();
        AtomicReference<String> second = new AtomicReference<>();

        try (MockedStatic<ConnectorUtils> connectorUtils = Mockito.mockStatic(ConnectorUtils.class)) {
            connectorUtils.when(() -> ConnectorUtils.lookupTemplateParamater(messageContext, Constant.TARGET))
                          .thenReturn(null);

            Mockito.doAnswer(invocation -> {
                if (first.get() == null) {
                    first.set(((Object) invocation.getArgument(1)).toString());
                } else {
                    second.set(((Object) invocation.getArgument(1)).toString());
                }
                return null;
            }).when(messageContext).setVariable(Mockito.anyString(), Mockito.any());

            connector.connect(messageContext);
            connector.connect(messageContext);
        }

        Assertions.assertNotEquals(first.get(), second.get(),
                "Each invocation must generate a unique UUID");
    }
}
