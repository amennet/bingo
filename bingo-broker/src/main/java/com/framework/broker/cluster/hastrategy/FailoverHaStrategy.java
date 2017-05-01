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

package com.framework.broker.cluster.hastrategy;

import com.framework.broker.cluster.LoadBalance;
import com.framework.remoting.protocol.RemotingCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Failover hastrategy strategy.
 */
public class FailoverHaStrategy extends AbstractHaStrategy {

    protected ThreadLocal<List<String>> referersHolder = new ThreadLocal<List<String>>() {
        @Override
        protected List<String> initialValue() {
            return new ArrayList<String>();
        }
    };


    protected List<String> selectReferers(RemotingCommand request, LoadBalance loadBalance) {
        List<String> referers = referersHolder.get();
        referers.clear();
        //loadBalance.select(request);
        return referers;
    }

    @Override
    public RemotingCommand call(RemotingCommand request, LoadBalance loadBalance) {

        List<String> referers = selectReferers(request, loadBalance);
        if (referers.isEmpty()) {
            throw new RuntimeException(String.format("FailoverHaStrategy No referers for request:%s, loadbalance:%s", request, loadBalance));
        }
        String refUrl = referers.get(0);
        // 先使用method的配置
        /*int tryCount =
                refUrl.getMethodParameter(request.getMethodName(), request.getParamtersDesc(), URLParamType.retries.getName(),
                        URLParamType.retries.getIntValue());
        // 如果有问题，则设置为不重试
        if (tryCount < 0) {
            tryCount = 0;
        }

        for (int i = 0; i <= tryCount; i++) {
            String refer = referers.get(i % referers.size());
            try {
                request.setRetries(i);
                return refer.call(request);
            } catch (RuntimeException e) {
                // 对于业务异常，直接抛出
                if (ExceptionUtil.isBizException(e)) {
                    throw e;
                } else if (i >= tryCount) {
                    throw e;
                }
                log.warn(String.format("FailoverHaStrategy Call false for request:%s error=%s", request, e.getMessage()));
            }
        }

        throw new BingoException("FailoverHaStrategy.call should not come here!");*/
        return null;
    }
}
