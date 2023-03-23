package com.bantanger.netty.chat.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 9:58
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    static Set<Channel> channelsList = new HashSet<>();

    /**
     * 监听客户端上线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 遍历所有在线客户端。通知有人上线
        channelsList.forEach(e -> {
            e.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + "上线了");
        });
        channelsList.add(ctx.channel());
    }

    /**
     * 监听客户端下线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 遍历所有在线客户端，通知有人下线
        channelsList.forEach(e -> {
            e.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + "下线了");
        });
        channelsList.remove(ctx.channel());
    }

    /**
     * 监听消息传输
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String message = (String) msg;

        channelsList.forEach(e -> {
            if (e == ctx.channel()) {
                e.writeAndFlush("[自己] ：" + message);
            } else {
                e.writeAndFlush("[客户端] " + ctx.channel().remoteAddress() + " : " + message);
            }
        });

    }

}
