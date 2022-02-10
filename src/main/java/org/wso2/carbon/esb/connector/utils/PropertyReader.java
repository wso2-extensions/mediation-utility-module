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

package org.wso2.carbon.esb.connector.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.util.ConnectorUtils;
import org.wso2.carbon.esb.connector.utils.exception.InvalidParameterValueException;

import java.util.Optional;

public class PropertyReader {

    private PropertyReader() {

    }

    /**
     * Reads a String parameter.
     *
     * @param mc           SimpleMessageContext.
     * @param parameterKey Key of the parameter.
     * @return Optional String of the parameter value.
     */
    public static Optional<String> getStringProperty(MessageContext mc, String parameterKey) {

        String parameter = (String) ConnectorUtils.lookupTemplateParamater(mc, parameterKey);
        if (StringUtils.isNotBlank(parameter)) {
            return Optional.of(parameter);
        }
        return Optional.empty();
    }

    /**
     * Reads an int parameter.
     *
     * @param mc           SimpleMessageContext.
     * @param parameterKey Key of the parameter.
     * @return Optional int of the parameter value.
     */
    public static Optional<Integer> getIntProperty(MessageContext mc, String parameterKey) {

        Optional<String> parameter = getStringProperty(mc, parameterKey);
        return parameter.map(s -> {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

    /**
     * Reads a enum parameter.
     *
     * @param mc           SimpleMessageContext.
     * @param parameterKey Key of the parameter.
     * @return Enum of the parameter value.
     */
    public static <E extends Enum<E>> E getEnumProperty(MessageContext mc, String parameterKey, Class<E> enumType,
                                                        E defaultValue) throws InvalidParameterValueException {

        Optional<String> property = getStringProperty(mc, parameterKey);
        if (property.isPresent()) {
            try {
                return Enum.valueOf(enumType, property.get());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new InvalidParameterValueException(String.format("Invalid Parameter Value %s=%s", parameterKey,
                        property.get()), e);
            }
        } else {
            return defaultValue;
        }
    }
}
