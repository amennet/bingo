package com.framework.register.zookeeper;

import com.framework.common.register.URL;

public interface ZookeeperTransporter {

	ZookeeperClient connect(URL url);

}
