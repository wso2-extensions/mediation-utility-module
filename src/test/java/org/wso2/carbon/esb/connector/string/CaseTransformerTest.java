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

package org.wso2.carbon.esb.connector.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wso2.carbon.esb.connector.string.utils.CaseTransformer;

@ExtendWith(MockitoExtension.class)
public class CaseTransformerTest {

    @Test
    void test_transformToUpperCase_returnsUpperCaseString() {

        String input = "qwerty123@$";
        String transformed = CaseTransformer.transformToUpperCase(input);
        Assertions.assertEquals(transformed, input.toUpperCase());

        input = "qWeRtY[]{}$@";
        transformed = CaseTransformer.transformToUpperCase(input);
        Assertions.assertEquals(transformed, input.toUpperCase());
    }

    @Test
    void test_transformToLowerCase_returnsLowerCaseString() {

        String input = "QWERTY123@$";
        String transformed = CaseTransformer.transformToLowerCase(input);
        Assertions.assertEquals(transformed, input.toLowerCase());

        input = "qWeRtY[]{}$@";
        transformed = CaseTransformer.transformToLowerCase(input);
        Assertions.assertEquals(transformed, input.toLowerCase());
    }
}
