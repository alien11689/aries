/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.plugin.test;

import org.apache.aries.blueprint.plugin.test.interfaces.ServiceA;
import org.apache.aries.blueprint.plugin.test.interfaces.ServiceB;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class MyBean3 {

    @Inject
    @Named("my1")
    ServiceA serviceA1;

    @Inject
    @Named("my2")
    ServiceA serviceA2;

    @Inject
    ServiceB serviceB;

    @Inject
    @Named("serviceB2Id")
    ServiceB serviceB2;

    @Inject
    MyProduced myProduced;

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void txNotSupported() {
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void txMandatory() {
    }

    @Transactional(Transactional.TxType.NEVER)
    public void txNever() {
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void txRequired() {
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void txRequiresNew() {
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public void txSupports() {
    }
}
