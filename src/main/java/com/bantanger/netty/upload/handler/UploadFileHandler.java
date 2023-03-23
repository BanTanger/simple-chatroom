package com.bantanger.netty.upload.handler;

import com.bantanger.netty.upload.FileDto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 9:58
 */
public class UploadFileHandler extends ChannelInboundHandlerAdapter {

    private String fileSaveAddress;

    /**
     * 监听消息传输
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileDto) {
            FileDto dto = (FileDto) msg;
            fileSaveAddress = "C://" + dto.getFileName();
            if (dto.getCommand() == 1) {
                File file = new File(fileSaveAddress);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } else if (dto.getCommand() == 2) {
                // 写入文件
                save2File(fileSaveAddress, dto.getBytes());
            }
        }
    }
    public static boolean save2File(String fname, byte[] msg){
        OutputStream fos = null;
        try{
            File file = new File(fname);
            File parent = file.getParentFile();
            boolean bool;
            if ((!parent.exists()) &
                    (!parent.mkdirs())) {
                return false;
            }
            fos = new FileOutputStream(file,true);
            fos.write(msg);
            fos.flush();
            return true;
        }catch (FileNotFoundException e){
            return false;
        }catch (IOException e){
            File parent;
            return false;
        }
        finally{
            if (fos != null) {
                try{
                    fos.close();
                }catch (IOException e) {}
            }
        }
    }

}
