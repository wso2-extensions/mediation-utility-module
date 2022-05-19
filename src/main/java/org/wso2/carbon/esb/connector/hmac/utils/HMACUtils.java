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

package org.wso2.carbon.esb.connector.hmac.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.esb.connector.hmac.utils.constants.Constant;
import org.wso2.carbon.esb.connector.utils.PayloadReader;
import org.wso2.carbon.esb.connector.utils.exception.NoSuchContentTypeException;
import org.wso2.carbon.esb.connector.utils.exception.PayloadNotFoundException;

import java.util.Optional;

public class HMACUtils {

    protected static Log log = LogFactory.getLog(HMACUtils.class);

    public static String getPayload(MessageContext messageContext, Optional<String> payloadFromOptional,
                                    Optional<String> customPayloadOptional) {

        String payload = "";
        if (payloadFromOptional.isPresent() && StringUtils.equalsIgnoreCase(payloadFromOptional.get(),
                Constant.PAYLOAD_FROM_DEFAULT)) {
            try {
                payload = PayloadReader.getPayload(messageContext);
            } catch (NoSuchContentTypeException e) {
                log.error("Invalid Content-Type: ", e);
            } catch (PayloadNotFoundException e) {
                log.error("No content in the message body", e);
            }
        } else {
            payload = customPayloadOptional.orElse("");
        }
        return payload;
    }
}
