package com.baidu.harry.fourteen;

/**
 * Created with IntelliJ IDEA.
 * User: chenyehui
 * Date: 14-11-6
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public class NettyMessage {

    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
