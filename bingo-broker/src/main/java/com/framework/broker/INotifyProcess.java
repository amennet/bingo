package com.framework.broker;

import com.framework.common.register.URL;

import java.util.List;

/**
 * Created by ZhangGe on 2017/4/27.
 */
public interface INotifyProcess {
    void process(List<URL> urls);
}
