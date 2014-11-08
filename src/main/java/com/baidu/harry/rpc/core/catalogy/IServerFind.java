package com.baidu.harry.rpc.core.catalogy;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-7
 * Time: 下午12:39
 * To change this template use File | Settings | File Templates.
 */
public interface IServerFind {

    public MutablePair<String, Integer> getServerHost(List<String> list);
}
