package com.baidu.harry.rpc.core.catalogy;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-7
 * Time: 下午5:04
 * To change this template use File | Settings | File Templates.
 */
public class RandomUtil {

    public static Integer getRandomInt(Integer no) {
        Random random = new Random();
        return random.nextInt(no);
    }
}
