/*
 *
 *  * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  *
 *  * WSO2 Inc. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.wso2.carbon.esb.connector.string;

import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.esb.connector.string.utils.constants.Constant;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.wso2.carbon.esb.connector.utils.PropertyReader.getStringProperty;

public class RegexMatcher extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {

        Optional<String> inputOptional = getStringProperty(messageContext, "string");
        Optional<String> regexOptional = getStringProperty(messageContext, "regex");
        Optional<String> saveToPropertyOptional = getStringProperty(messageContext, "target");
        String input = inputOptional.orElse("");
        String regex = regexOptional.orElse("");
        String saveToProperty = saveToPropertyOptional.orElse(Constant.saveToPropertyRegexMatcher);
        try {
            //check the string with the regex
            Boolean matching = matches(regex, input);
            messageContext.setProperty(saveToProperty, matching.toString());
        } catch (PatternSyntaxException e) {
            log.error("Invalid regular expression:", e);
        }
    }

    private Boolean matches(String regex, String input) {

        return Pattern.matches(regex, input);
    }
}

