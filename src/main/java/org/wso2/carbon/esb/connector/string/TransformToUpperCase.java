/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 */

package org.wso2.carbon.esb.connector.string;

import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.esb.connector.string.utils.constants.Constant;

import java.util.Optional;

import static org.wso2.carbon.esb.connector.string.utils.CaseTransformer.transformToUpperCase;
import static org.wso2.carbon.esb.connector.utils.PropertyReader.getStringProperty;

public class TransformToUpperCase extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        Optional<String> stringOptional = getStringProperty(messageContext, "string");
        Optional<String> saveToPropertyOptional = getStringProperty(messageContext, "target");
        String string = stringOptional.orElse("");
        String saveToProperty = saveToPropertyOptional.orElse(Constant.SAVE_TO_PROPERTY_CASE_CHANGER);
        //transform the string to uppercase
        String transformedString = transformToUpperCase(string);
        messageContext.setProperty(saveToProperty, transformedString);
    }
}
