package com.bantanger.netty;

import com.bantanger.netty.upload.server.UploadFileServer;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 10:06
 */
public class Starter {

    public static void main(String[] args) throws Exception {
//        new DiscardServer(9001).run();
//        new HeartbeatServer(9001).run();
        new UploadFileServer(9001).run();
    }

}
