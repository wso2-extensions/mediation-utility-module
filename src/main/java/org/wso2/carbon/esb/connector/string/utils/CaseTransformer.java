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

package org.wso2.carbon.esb.connector.string.utils;

import org.apache.commons.lang.StringUtils;


public class CaseTransformer {

    private CaseTransformer() {

    }

    /**
     * Transform the string to lowercase.
     *
     * @param string String to transform.
     * @return String transformed string.
     */
    public static String transformToLowerCase(String string) {

        return StringUtils.lowerCase(string);
    }

    /**
     * Transform the string to uppercase
     *
     * @param string String to transform.
     * @return String transformed string.
     */
    public static String transformToUpperCase(String string) {

        return StringUtils.upperCase(string);
    }
}
