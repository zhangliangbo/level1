package xxl.reactornetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import xxl.firmware.FrameTwoByte;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class RadarServerDemo {
    public static void main(String[] args) throws InterruptedException {
        final int port = 8080;
        NioEventLoopGroup parent = new NioEventLoopGroup();
        NioEventLoopGroup child = new NioEventLoopGroup();
        System.err.println("start");
        new ServerBootstrap().channel(NioServerSocketChannel.class)
                .group(parent, child)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                            Disposable disposable = null;

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                super.channelActive(ctx);
                                System.err.println("active:" + ctx.channel().remoteAddress().toString());
                                Observable.interval(1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                        disposable = d;
                                    }

                                    @Override
                                    public void onNext(@NonNull Long aLong) {
                                        FrameTwoByte frameTwoByte = new FrameTwoByte(
                                                (byte) 0xA5,
                                                (short) 0,
                                                (short) 1,
                                                (byte) 1,
                                                String.valueOf(aLong).getBytes()
                                        );
                                        byte[] bytes = frameTwoByte.toFrame();
                                        System.err.println(aLong);
                                        System.err.println(Arrays.toString(bytes));
                                        ctx.writeAndFlush(Unpooled.wrappedBuffer(bytes));
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                super.exceptionCaught(ctx, cause);
                                System.err.println(cause.getMessage());
                                if (disposable != null) {
                                    disposable.dispose();
                                }
                            }

                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

                            }
                        });
                    }
                })
                .bind(port)
                .sync()
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.err.println("bind");
                    }
                })
                .channel()
                .closeFuture()
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        parent.shutdownGracefully();
                        child.shutdownGracefully();
                    }
                })
                .sync();
        System.err.println("end");
    }
}
