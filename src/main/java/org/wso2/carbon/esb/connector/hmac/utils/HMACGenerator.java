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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACGenerator {

    private HMACGenerator() {

    }

    private static final Map<String, Mac> macInstancesMap = new ConcurrentHashMap<>();

    /**
     * Generates a signature for the payload.
     *
     * @param payload   String.
     * @param secret    Secret used to sign the payload.
     * @param algorithm Signing algorithm.
     * @return Signature for the payload.
     */
    public static String generateSignature(String payload, String secret, String algorithm) throws Exception {

        try {
            Mac mac = getMacInstance(algorithm);
            if (StringUtils.isBlank(secret) || StringUtils.isEmpty(secret)) {
                throw new Exception();
            }
            final SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), algorithm);
            mac.init(signingKey);
            return toHexString(mac.doFinal(payload.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(e);
        } catch (InvalidKeyException e) {
            throw new InvalidKeyException(e);
        }
    }

    //Convert the byte array to string
    private static String toHexString(byte[] bytes) {

        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    //Returns a MAC instance for the given algorithm
    private static Mac getMacInstance(String algorithm) throws NoSuchAlgorithmException {

        Mac macInstance = macInstancesMap.get(algorithm);
        if (macInstance == null) {
            macInstance = Mac.getInstance(algorithm);
            macInstancesMap.put(algorithm, macInstance);
        }
        return macInstance;
    }
}
