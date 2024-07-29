package com.keriko.coderace.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 * @author keriko
 * @from keriko_admin
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}