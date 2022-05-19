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

package org.wso2.carbon.esb.connector.math;

import org.apache.commons.lang.math.IntRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wso2.carbon.esb.connector.math.utils.RandomNumberGenerator;
import org.wso2.carbon.esb.connector.math.utils.exception.InvalidBoundException;

@ExtendWith(MockitoExtension.class)
public class RandomIntegerGeneratorTest {

    @Test
    void test_RandomNumberGenerator_ValidOriginBound_randomNumberGeneratedInCorrectRange() throws InvalidBoundException {

        int origin = 0;
        int bound = 5;
        int rand = RandomNumberGenerator.generateRandomInteger(origin, bound);
        IntRange range = new IntRange(origin, bound);
        Assertions.assertTrue(range.containsInteger(rand));
    }

    @Test
    void test_RandomNumberGenerator_invalidOriginBound_throwIllegalArgumentException() {

        int origin = 10;
        int bound = 9;
        Assertions.assertThrows(InvalidBoundException.class, () -> RandomNumberGenerator.generateRandomInteger(origin
                , bound));
    }
}
