package com.baidu.harry.rpc.core.catalogy;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-7
 * Time: 下午12:45
 * To change this template use File | Settings | File Templates.
 */
public class RandomAccessServerFind implements IServerFind {

    @Override
    public MutablePair<String, Integer> getServerHost(List<String> list) {
        String value = list.get(RandomUtil.getRandomInt(list.size()));
        String[] temp = value.split(":");
        return new MutablePair<String, Integer>(temp[0], Integer.parseInt(temp[1]));
    }
}
