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

package com.framework.broker.cluster;

import com.framework.common.register.URL;
import com.framework.remoting.protocol.RemotingCommand;

import java.util.List;

/**
 * Cluster is a service broker, used to
 */
public interface Cluster {

    void init();

    void setUrl(URL url);

    void setLoadBalance(LoadBalance loadBalance);

    void setHaStrategy(HaStrategy haStrategy);

    void onRefresh(List<String> servers);

    List<String> getServers();

    LoadBalance getLoadBalance();

    RemotingCommand call(RemotingCommand request);

    boolean isAvailable();
}
