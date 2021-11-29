package com.ff.doto;

import com.ff.entity.User;
import lombok.Data;

/**
 * @author FF
 * @date 2021/11/27
 * @TIME:16:50
 */
@Data
public class LoginDto extends User {

    private String code;
}
