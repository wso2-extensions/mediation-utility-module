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

package org.wso2.carbon.esb.connector.hmac.utils;

import org.apache.commons.lang.StringUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACVerify {

    private HMACVerify() {

    }

    /**
     * Verify the payload using the signature
     *
     * @param payload   String
     * @param secret    String
     * @param algorithm Signing algorithm
     * @return boolean value of verified or not.
     */
    public static boolean verify(String payload, String secret, String algorithm, String signature) throws NoSuchAlgorithmException, InvalidKeyException {
        //generate a signature for the payload using the algorithm and secret provided.
        String payloadSignature = HMACGenerator.generateSignature(payload, secret, algorithm);
        return StringUtils.equals(signature, payloadSignature);
    }
}
