package com.bantanger.netty.upload;

import lombok.Data;

/**
 * @author BanTanger 半糖
 * @Date 2023/3/23 20:10
 */
@Data
public class FileDto {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 指令【1. 请求创建文件 2. 传输文件】
     */
    private Integer command;

    /**
     * 文件字节数组：结合实际应用可使用非对称加密来确保传输安全性
     */
    private byte[] bytes;
}
