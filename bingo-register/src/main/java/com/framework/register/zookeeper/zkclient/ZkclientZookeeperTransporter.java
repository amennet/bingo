package com.framework.register.zookeeper.zkclient;


import com.framework.common.register.URL;
import com.framework.register.zookeeper.ZookeeperClient;
import com.framework.register.zookeeper.ZookeeperTransporter;

public class ZkclientZookeeperTransporter implements ZookeeperTransporter {

	public ZookeeperClient connect(URL url) {
		return new ZkclientZookeeperClient(url);
	}

}
