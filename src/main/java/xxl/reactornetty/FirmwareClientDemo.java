package xxl.reactornetty;

import io.vavr.Function2;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;
import reactor.netty.Connection;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.tcp.TcpClient;
import xxl.firmware.FrameContainer;
import xxl.firmware.FrameTwoByte;
import xxl.firmware.ICache;

import java.util.Scanner;
import java.util.function.Consumer;

public class FirmwareClientDemo {
    public static void main(String[] args) {
        FrameContainer container = new FrameContainer((byte) 0xA5, 1024, new ICache.OnNewFrameListener() {
            @Override
            public void onNewFrame(byte[] frame) {
                FrameTwoByte in = FrameTwoByte.parseFrame(frame);
                System.out.println("client receive: " + new String(in.getData()));
            }
        });
        Connection connection = TcpClient.create()
                .doOnConnected(new Consumer<Connection>() {
                    @Override
                    public void accept(Connection connection) {
                        System.err.println("connected");
                    }
                })
                .handle(new Function2<NettyInbound, NettyOutbound, Publisher<Void>>() {
                    @Override
                    public Publisher<Void> apply(NettyInbound nettyInbound, NettyOutbound nettyOutbound) {
                        return Flux.merge(
                                nettyInbound.receive().asByteArray().doOnNext(new Consumer<byte[]>() {
                                    @Override
                                    public void accept(byte[] s) {
                                        container.append(s);
                                    }
                                }).subscribeOn(Schedulers.newSingle("FirmwareRead")),
                                nettyOutbound.sendByteArray(Flux.create(new Consumer<FluxSink<byte[]>>() {
                                    @Override
                                    public void accept(FluxSink<byte[]> sink) {
                                        Scanner sc = new Scanner(System.in);
                                        while (true) {
                                            System.err.println("请输入发送内容或quit退出");
                                            String line = sc.nextLine();
                                            if ("quit".equals(line)) {
                                                break;
                                            } else {
                                                FrameTwoByte out = new FrameTwoByte((byte) 0xA5, (byte) 0x02, (byte) 0x01, (byte) 0x20, line.getBytes());
                                                sink.next(out.toFrame());
                                            }
                                        }
                                        sink.complete();
                                    }
                                }).subscribeOn(Schedulers.newSingle("FirmwareWrite")))).then();
                    }
                })
                .host("localhost")
                .port(12307)
                .connectNow();
        connection.onDispose().block();
    }
}
