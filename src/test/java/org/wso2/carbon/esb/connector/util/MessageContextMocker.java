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

package org.wso2.carbon.esb.connector.util;

import org.apache.axiom.util.UIDGenerator;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.InOutAxisOperation;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.core.axis2.Axis2SynapseEnvironment;
import org.apache.synapse.core.axis2.MessageContextCreatorForAxis2;
//import org.wso2.carbon.apimgt.gateway.handlers.WebsocketUtil;

public class MessageContextMocker {

    public static MessageContext getSynapseMessageContext() throws AxisFault {

        org.apache.axis2.context.MessageContext axis2MsgCtx = createAxis2MessageContext();
        ServiceContext svcCtx = new ServiceContext();
        OperationContext opCtx = new OperationContext(new InOutAxisOperation(), svcCtx);
        axis2MsgCtx.setServiceContext(svcCtx);
        axis2MsgCtx.setOperationContext(opCtx);
        axis2MsgCtx.setProperty("tenantDomain", "carbon.super");
        MessageContextCreatorForAxis2.setSynEnv(getSynapseEnvironment(axis2MsgCtx));
        MessageContextCreatorForAxis2.setSynConfig(getSynapseConfiguration(axis2MsgCtx));
        //        return MessageContextCreatorForAxis2.getSynapseMessageContext(axis2MsgCtx);
        return new Axis2MessageContext(axis2MsgCtx, getSynapseConfiguration(axis2MsgCtx),
                getSynapseEnvironment(axis2MsgCtx));
    }

    private static org.apache.axis2.context.MessageContext createAxis2MessageContext() throws AxisFault {

        org.apache.axis2.context.MessageContext axis2MsgCtx = new org.apache.axis2.context.MessageContext();
        axis2MsgCtx.setMessageID(UIDGenerator.generateURNString());

        AxisConfiguration axisConfig = new AxisConfiguration();
        axisConfig.addParameter(new Parameter("synapse.env",
                new Axis2SynapseEnvironment(getSynapseConfiguration(axis2MsgCtx))));
        ConfigurationContext configContext = new ConfigurationContext(axisConfig);
        axis2MsgCtx.setConfigurationContext(configContext);
        axis2MsgCtx.setProperty(org.apache.axis2.context.MessageContext.CLIENT_API_NON_BLOCKING, Boolean.TRUE);
        axis2MsgCtx.setServerSide(true);
        return axis2MsgCtx;
    }

    private static SynapseEnvironment getSynapseEnvironment(org.apache.axis2.context.MessageContext axisMsgCtx) {

        AxisConfiguration axisCfg = axisMsgCtx.getConfigurationContext().getAxisConfiguration();
        Parameter param = axisCfg.getParameter("synapse.env");
        return param != null ? (SynapseEnvironment) param.getValue() : null;
    }

    private static SynapseConfiguration getSynapseConfiguration(org.apache.axis2.context.MessageContext axisMsgCtx) {

        AxisConfiguration axisCfg = axisMsgCtx.getConfigurationContext().getAxisConfiguration();
        Parameter param = axisCfg.getParameter("synapse.config");
        return param != null ? (SynapseConfiguration) param.getValue() : null;
    }
}
