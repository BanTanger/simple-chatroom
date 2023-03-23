package com.bantanger.netty.upload.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 18:38
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private int readTimeOut = 0;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
//        System.out.println("触发了：" + event.state() + "事件");
        // 触发读事件便记录
        if (event.state() == IdleState.READER_IDLE) {
            readTimeOut ++;
        }
        if (readTimeOut >= 3) {
            System.out.println("超时超过3次，断开连接");
            ctx.close();
        }
    }

}
