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

package org.wso2.carbon.esb.connector.math.utils;

import org.wso2.carbon.esb.connector.math.utils.exception.InvalidBoundException;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumberGenerator {

    private RandomNumberGenerator() {

    }

    /**
     * Generates a random integer number in a given range.
     *
     * @param origin Lower bound for the random number.
     * @param bound  Upper bound for the random number.
     * @return random integer.
     */
    public static int generateRandomInteger(int origin, int bound) throws InvalidBoundException {

        try {
            return ThreadLocalRandom.current().nextInt(origin, bound);
        } catch (IllegalArgumentException e) {
            throw new InvalidBoundException(e);
        }
    }
}
