package com.sesac.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/** Class Token
 * Redis 에 저장할 refresh token
 * key: username, value: refresh token
 */
@Getter
@AllArgsConstructor
@RedisHash
public class RedisToken implements Serializable {
    private static final long serialVersionUID = -7353484588260422449L;
    private String email;
    private String refreshToken;
}
