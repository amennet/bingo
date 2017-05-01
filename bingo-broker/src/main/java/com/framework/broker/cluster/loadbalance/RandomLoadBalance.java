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

import com.framework.common.register.URL;
import com.framework.remoting.protocol.RemotingCommand;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * random load balance.
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    public URL doSelect(List<URL> servers, RemotingCommand request) {
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * servers.size());
        for (int i = 0; i < servers.size(); i++) {
            URL ref = servers.get((i + idx) % servers.size());
            return ref;
        }
        return null;
    }
}
