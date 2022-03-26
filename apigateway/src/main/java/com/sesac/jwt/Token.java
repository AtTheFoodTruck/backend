package com.sesac.jwt;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/** Class Token
 * Redis 에 저장할 refresh token
 * key: username, value: refresh token
 */
@Data
@RedisHash
public class Token implements Serializable {
    private static final long serialVersionUID = -7353484588260422449L;
    private String username;
    private String refreshToken;
}
