package com.baidu.harry.fourteen;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeCFactory {
    public static NettyMarshallingDecoder buildMarshallingDecoder() {
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        return new NettyMarshallingDecoder(new DefaultUnmarshallerProvider(marshallerFactory, configuration), 1024);
    }

    public static NettyMarshallingEncoder buildMarshallingEncoder() {
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        return new NettyMarshallingEncoder(new DefaultMarshallerProvider(marshallerFactory, configuration));
    }
}