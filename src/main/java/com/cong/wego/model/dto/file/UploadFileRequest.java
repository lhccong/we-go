package com.cong.wego.model.dto.file;

import java.io.Serializable;
import lombok.Data;

/**
 * 文件上传请求
 * # @author <a href="https://github.com/lhccong">程序员聪</a>
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;

    private static final long serialVersionUID = 1L;
}