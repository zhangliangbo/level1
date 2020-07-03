package xxl.reactornetty;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RadarDemo {
    public static void main(String[] args) throws InterruptedException {
        final String host = "localhost";
        final int port = 8080;
        final NioEventLoopGroup group = new NioEventLoopGroup();
        System.err.println("start connected.");
        new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                super.channelActive(ctx);
                                System.err.println("active");
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                super.channelInactive(ctx);
                                System.err.println("inactive");
                            }

                            @Override
                            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                super.channelRegistered(ctx);
                                System.err.println("registered");
                            }

                            @Override
                            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                                super.channelUnregistered(ctx);
                                System.err.println("unregistered");
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                System.err.println("read:" + new String(ByteBufUtil.getBytes(msg)));
                            }
                        });
                    }
                })
                .connect(host, port)
                .sync()
                .channel()
                .closeFuture()
                .addListener((ChannelFutureListener) future -> group.shutdownGracefully())
                .sync();
        System.err.println("end connected.");
    }
}
