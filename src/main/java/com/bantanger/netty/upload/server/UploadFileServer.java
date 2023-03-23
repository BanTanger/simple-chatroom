package com.bantanger.netty.upload.server;

import com.bantanger.netty.upload.handler.HeartbeatHandler;
import com.bantanger.netty.upload.handler.UploadFileHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 9:57
 */
public class UploadFileServer {

    private int port;

    public UploadFileServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            Charset gbk = Charset.forName("GBK");
                            //给pipeline管道设置处理器
                            socketChannel.pipeline().addLast("decoder", new StringDecoder(gbk));
                            socketChannel.pipeline().addLast("encoder", new StringEncoder(gbk));
                            // 心跳机制保活
                            socketChannel.pipeline().addLast(new IdleStateHandler(2, 2, 5));
                            socketChannel.pipeline().addLast(new HeartbeatHandler());
                            socketChannel.pipeline().addLast(new UploadFileHandler());
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("tcp start success");
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
