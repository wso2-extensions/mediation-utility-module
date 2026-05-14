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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * Regression guard for GitHub Issue #196.
 *
 * A debug {@code <log level="full">} mediator was accidentally left inside the
 * {@code string/randomUUID.xml} connector template, causing every invocation of
 * {@code utility.string.UUID} to emit a verbose INFO log line (including the full
 * SOAP envelope) at INFO level.
 *
 * This test parses the template XML directly and asserts the log element is absent,
 * preventing the regression from being silently re-introduced.
 */
public class RandomUUIDTemplateTest {

    private static final String TEMPLATE_RESOURCE = "/string/randomUUID.xml";
    private static Document templateDoc;

    @BeforeAll
    static void loadTemplate() throws Exception {
        InputStream is = RandomUUIDTemplateTest.class.getResourceAsStream(TEMPLATE_RESOURCE);
        Assertions.assertNotNull(is, "Template resource not found on classpath: " + TEMPLATE_RESOURCE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        templateDoc = builder.parse(is);
    }

    // -----------------------------------------------------------------------
    // Issue #196 regression: no <log> element must exist in the template
    // -----------------------------------------------------------------------

    /**
     * Asserts that no {@code <log>} mediator is present anywhere in the template.
     * Prior to the fix, a {@code <log level="full">} block was left inside the
     * sequence, emitting the entire SOAP envelope on every UUID call.
     */
    @Test
    void test_randomUUIDTemplate_hasNoLogMediator() {
        NodeList logElements = templateDoc.getElementsByTagNameNS("*", "log");
        Assertions.assertEquals(0, logElements.getLength(),
                "randomUUID.xml must not contain any <log> mediator. " +
                "A <log level=\"full\"> left from debugging causes verbose INFO logs on every invocation (Issue #196).");
    }

    /**
     * Narrower variant: asserts there is no {@code <log level="full">} element specifically.
     */
    @Test
    void test_randomUUIDTemplate_hasNoFullLevelLog() {
        NodeList logElements = templateDoc.getElementsByTagNameNS("*", "log");
        for (int i = 0; i < logElements.getLength(); i++) {
            String level = logElements.item(i).getAttributes().getNamedItem("level") != null
                    ? logElements.item(i).getAttributes().getNamedItem("level").getNodeValue()
                    : "";
            Assertions.assertNotEquals("full", level,
                    "randomUUID.xml must not contain <log level=\"full\"> (Issue #196 regression).");
        }
    }

    // -----------------------------------------------------------------------
    // Template structure sanity checks
    // -----------------------------------------------------------------------

    @Test
    void test_randomUUIDTemplate_hasCorrectTemplateName() {
        NodeList templates = templateDoc.getElementsByTagNameNS("*", "template");
        Assertions.assertEquals(1, templates.getLength(), "Expected exactly one <template> element");
        String name = templates.item(0).getAttributes().getNamedItem("name").getNodeValue();
        Assertions.assertEquals("randomUUID", name,
                "Template name must be 'randomUUID'");
    }

    @Test
    void test_randomUUIDTemplate_hasTargetVariableParameter() {
        NodeList params = templateDoc.getElementsByTagNameNS("*", "parameter");
        boolean found = false;
        for (int i = 0; i < params.getLength(); i++) {
            org.w3c.dom.Node nameAttr = params.item(i).getAttributes().getNamedItem("name");
            if (nameAttr != null && "targetVariable".equals(nameAttr.getNodeValue())) {
                found = true;
                break;
            }
        }
        Assertions.assertTrue(found, "Template must declare a <parameter name=\"targetVariable\"/>");
    }

    @Test
    void test_randomUUIDTemplate_hasRandomUUIDClassMediator() {
        NodeList classElements = templateDoc.getElementsByTagNameNS("*", "class");
        Assertions.assertTrue(classElements.getLength() > 0,
                "Template must contain a <class> mediator that invokes RandomUUID");
        String className = classElements.item(0).getAttributes().getNamedItem("name").getNodeValue();
        Assertions.assertEquals("org.wso2.carbon.esb.connector.string.RandomUUID", className,
                "Class mediator must reference RandomUUID implementation");
    }

    @Test
    void test_randomUUIDTemplate_hasExactlyOneClassMediator() {
        NodeList classElements = templateDoc.getElementsByTagNameNS("*", "class");
        Assertions.assertEquals(1, classElements.getLength(),
                "Template should contain exactly one <class> mediator");
    }
}
