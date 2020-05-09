package xxl.mqtt;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyMqttClient {

    private static final String HOST = System.getProperty("host", "rabbitmq.xxlun.com");
    private static final int PORT = Integer.parseInt(System.getProperty("port", "1883"));
    private static final String CLIENT_ID = System.getProperty("clientId", "netty");
    private static final String USER_NAME = System.getProperty("userName", "mqtt");
    private static final String PASSWORD = System.getProperty("password", "mqtt");

    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                    ch.pipeline().addLast("decoder", new MqttDecoder());
                    ch.pipeline().addLast("heartBeatHandler", new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast("handler", new MqttHeartBeatClientHandler(CLIENT_ID, USER_NAME, PASSWORD));
                }
            });

            ChannelFuture f = b.connect(HOST, PORT).sync();
            System.out.println("Client connected");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
