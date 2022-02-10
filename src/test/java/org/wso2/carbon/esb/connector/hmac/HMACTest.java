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

package org.wso2.carbon.esb.connector.hmac;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wso2.carbon.esb.connector.hmac.utils.HMACGenerator;

import java.security.NoSuchAlgorithmException;

@ExtendWith(MockitoExtension.class)
public class HMACTest {

    @Test
    void test_hmacGenerator_validAlgorithm() throws Exception {

        String payload = "abc";
        String secret = "123";
        String algorithm = "HmacSHA1";
        String actualHmac = HMACGenerator.generateSignature(payload, secret, algorithm);

        String expectedHmac = "540b0c53d4925837bd92b3f71abe7a9d70b676c4";

        Assertions.assertEquals(actualHmac, expectedHmac);
    }

    @Test
    void test_hmacGenerator_invalidAlgorithm_throwsNoSuchAlgorithmException() {

        String payload = "abc";
        String secret = "123";
        String algorithm = "Hmacsha";
        Assertions.assertThrows(NoSuchAlgorithmException.class, () -> HMACGenerator.generateSignature(payload, secret
                , algorithm));
    }
}
