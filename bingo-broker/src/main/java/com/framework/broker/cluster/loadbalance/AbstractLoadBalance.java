/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.framework.broker.cluster.loadbalance;


import com.framework.broker.cluster.LoadBalance;
import com.framework.common.register.URL;
import com.framework.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * loadbalance
 */

public abstract class AbstractLoadBalance implements LoadBalance {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int MAX_REFERER_COUNT = 10;

    @Override
    public URL select(Set<URL> servers, RemotingCommand request) {
        List<URL> serverList = new ArrayList<>(servers);

        URL ref = null;
        if (serverList.size() > 1) {
            ref = doSelect(serverList, request);

        } else if (servers.size() == 1) {
            ref = serverList.get(0);
        }

        if (ref != null) {
            return ref;
        }
        throw new RuntimeException(this.getClass().getSimpleName() + " No available referers for call request:" + request);
    }

    @Override
    public void setWeightString(String weightString) {
        log.info("ignore weightString:" + weightString);
    }

    protected abstract URL doSelect(List<URL> servers, RemotingCommand request);
}
