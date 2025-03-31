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

package org.wso2.carbon.esb.connector.hmac;

import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;
import org.wso2.carbon.esb.connector.hmac.utils.HMACGenerator;
import org.wso2.carbon.esb.connector.hmac.utils.HMACUtils;
import org.wso2.carbon.esb.connector.hmac.utils.constants.Constant;
import org.wso2.carbon.esb.connector.hmac.utils.constants.HMACAlgorithm;
import org.wso2.carbon.esb.connector.hmac.utils.exception.InvalidSecretException;
import org.wso2.carbon.esb.connector.utils.PayloadReader;
import org.wso2.carbon.esb.connector.utils.PropertyReader;
import org.wso2.carbon.esb.connector.utils.exception.InvalidParameterValueException;
import org.wso2.carbon.esb.connector.utils.exception.NoSuchContentTypeException;
import org.wso2.carbon.esb.connector.utils.exception.PayloadNotFoundException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class Sign extends AbstractConnector {

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {
        //Reading properties from the message context.
        Optional<String> payloadFromOptional = PropertyReader.getStringProperty(messageContext, Constant.PAYLOAD);
        Optional<String> customPayloadOptional = PropertyReader.getStringProperty(messageContext,
                Constant.CUSTOM_PAYLOAD);
        Optional<String> secretOptional = PropertyReader.getStringProperty(messageContext, Constant.SECRET);
        Optional<String> saveToVariableOptional = PropertyReader.getStringProperty(messageContext, Constant.TARGET);
        String payload = null;
        try {
            payload = HMACUtils.getPayload(messageContext, payloadFromOptional, customPayloadOptional);
            String secret = secretOptional.orElse("");
            String saveToVariable = saveToVariableOptional.orElse(Constant.SAVE_SIGN_RESULT_TO);
            HMACAlgorithm algorithm = PropertyReader.getEnumProperty(messageContext, Constant.ALGORITHM,
                    HMACAlgorithm.class, HMACAlgorithm.HMACSHA1);
            //Generate signature for the payload
            String sign = HMACGenerator.generateSignature(payload, secret, algorithm.toString());
            messageContext.setVariable(saveToVariable, sign);
        } catch (InvalidParameterValueException e) {
            log.error("Parameter value is invalid", e);
        } catch (NoSuchContentTypeException e) {
            log.error("Invalid Content-Type: ", e);
        } catch (PayloadNotFoundException e) {
            log.error("No content in the message body", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("Invalid Algorithm: ", e);
        } catch (InvalidSecretException | InvalidKeyException e) {
            log.error("Invalid secret provided", e);
        }
    }
}
