/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.plugin.test;

import org.apache.aries.blueprint.annotation.bean.Activation;
import org.apache.aries.blueprint.annotation.bean.Bean;
import org.apache.aries.blueprint.annotation.config.ConfigProperty;
import org.apache.aries.blueprint.plugin.test.interfaces.ServiceA;
import org.apache.aries.blueprint.plugin.test.interfaces.ServiceB;
import org.ops4j.pax.cdi.api.OsgiService;

import javax.inject.Inject;
import javax.inject.Named;

@Bean(activation = Activation.EAGER)
public class BeanWithSetters {

    @Inject
    @Named("my1")
    public void setServiceA1(ServiceA serviceA1) {
    }

    @Inject
    @Named("my2")
    public void setServiceA2(ServiceA serviceA2) {
    }

    @Inject
    public void setServiceB(ServiceB serviceB) {
    }

    @Inject
    @Named("serviceB2Id")
    public void setServiceB2(ServiceB serviceB2) {
    }

    public void setUseless(MyProduced myProduced) {
    }

    @Inject
    public void setIOnlyHaveSetPrefix() {
    }

    @Inject
    public void setIhaveMoreThenOneParameter(String a, String b) {
    }

    @ConfigProperty("test")
    public void setIOnlyHaveSetPrefixValue() {
    }

    @ConfigProperty("test")
    public void setIhaveMoreThenOneParameterValue(String a, String b) {
    }

    @ConfigProperty("test")
    public void setMyValue(String v) {
    }

    @Inject
    public void setServiceBRef(@OsgiService(filter = "(type=B1Ref)") ServiceB serviceBRef) {
    }

    @Inject
    @Named("serviceB2IdRef")
    @OsgiService(filter = "(type=B2Ref)")
    public void setServiceB2Ref(ServiceB serviceB2Ref) {
    }

    @Inject
    @OsgiService(filter = "B3Ref")
    public void setServiceB3Ref(ServiceB serviceB3Ref) {
    }
}
