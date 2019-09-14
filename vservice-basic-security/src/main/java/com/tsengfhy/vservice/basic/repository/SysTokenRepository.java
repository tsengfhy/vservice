package com.tsengfhy.vservice.basic.repository;

import com.tsengfhy.vservice.basic.domain.SysToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public interface SysTokenRepository extends AgileJpaRepository<SysToken, String>, PersistentTokenRepository {
}
