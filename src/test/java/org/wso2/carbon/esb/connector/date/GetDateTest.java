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

package org.wso2.carbon.esb.connector.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wso2.carbon.esb.connector.date.utils.Date;
import org.wso2.carbon.esb.connector.date.utils.exception.IllegalDateFormatException;

import java.text.Format;
import java.text.SimpleDateFormat;


@ExtendWith(MockitoExtension.class)
class GetDateTest {


    @Test
    void test_getDate_legalDateFormat() throws IllegalDateFormatException {

        String actualDate = Date.getDate("yyyy/MM/dd HH:mm:ss");
        Format formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final String expectedDate = formatter.format(new java.util.Date());
        Assertions.assertEquals(actualDate, expectedDate);
    }

    @Test
    void test_getDate_illegalDateFormat() {

        Assertions.assertThrows(IllegalDateFormatException.class, () -> Date.getDate("aff/dsf/asf"));

        Assertions.assertThrows(IllegalDateFormatException.class, () -> Date.getDate("x@#"));
    }
}
