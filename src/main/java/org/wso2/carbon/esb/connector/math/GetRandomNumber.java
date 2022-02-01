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

package org.wso2.carbon.esb.connector.math;

import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.esb.connector.math.utils.RandomNumberGenerator;
import org.wso2.carbon.esb.connector.math.utils.constants.Constant;

import java.util.Optional;

import static org.wso2.carbon.esb.connector.utils.PropertyReader.getIntProperty;
import static org.wso2.carbon.esb.connector.utils.PropertyReader.getStringProperty;

public class GetRandomNumber extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {
        //Read the properties form message context
        Optional<Integer> originOptional = getIntProperty(messageContext, "lowerBound");
        Optional<Integer> boundOptional = getIntProperty(messageContext, "upperBound");
        Optional<String> saveToPropertyOptional = getStringProperty(messageContext, "target");
        String saveTo = saveToPropertyOptional.orElse(Constant.saveToProperty);
        int origin = originOptional.orElse(Constant.intMin);
        int bound = boundOptional.orElse(Constant.intMax);
        int randomNumber;
        try {
            //Get the random number
            randomNumber = RandomNumberGenerator.generateRandomInteger(origin, bound);
            messageContext.setProperty(saveTo, randomNumber);
        } catch (IllegalArgumentException e) {
            log.error(e);
        }
    }
}
