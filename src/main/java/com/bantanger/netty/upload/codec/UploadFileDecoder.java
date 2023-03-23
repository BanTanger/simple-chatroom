package com.bantanger.netty.upload.codec;

import com.bantanger.netty.upload.FileDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 18:53
 */
public class UploadFileDecoder extends ByteToMessageDecoder {

    /**
     * 请求文件上传，创建文件，并将文件保存到本地磁盘
     * command 4byte + fileName 4byte = 8 byte
     * command == 2 上传文件
     * 数据长度 + 数据实体
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 读取自定义协议包的数据长度，如果数据长度小于8，则不接收
        if (in.readableBytes() < 8) {
            return;
        }
        // command 4
        int command = in.readInt();

        FileDto fileDto = new FileDto();

        // fileNameLen 4
        int fileNameLen = in.readInt();

        if (in.readableBytes() < fileNameLen) {
            // 数据长度如果不足，重置已读指针
            in.resetReaderIndex();
            return ;
        }

        byte[] data = new byte[fileNameLen];
        in.readBytes(data);
        String fileName = new String(data);
        fileDto.setCommand(command);
        fileDto.setFileName(fileName);

        // 上传文件
        if(command == 2) {
            int dataLen = in.readInt();
            if (in.readableBytes() < dataLen) {
                // 数据长度如果不足，重置已读指针
                in.resetReaderIndex();
                return ;
            }
            byte[] fileData = new byte[dataLen];
            in.readBytes(fileData);
            fileDto.setBytes(fileData);
        }
        // 标记当前已读指针位置
        in.markReaderIndex();
        // 解码后的数据添加到handler pipeline处理链的下一个Channel Handler中进行后续处理
        out.add(in);
    }

}
