package com.tsengfhy.vservice.basic.repository.impl;

import com.tsengfhy.vservice.basic.domain.SysToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
public class SysTokenRepositoryImpl implements PersistentTokenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void createNewToken(PersistentRememberMeToken token) {
        SysToken sysToken = new SysToken();
        sysToken.setId(token.getSeries());
        sysToken.setUsername(token.getUsername());
        sysToken.setToken(token.getTokenValue());
        sysToken.setLastModifiedDate(new Timestamp(token.getDate().getTime()));
        entityManager.persist(sysToken);
    }

    @Override
    @Transactional
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        SysToken sysToken = entityManager.find(SysToken.class, series);
        if (sysToken != null) {
            sysToken.setToken(tokenValue);
            sysToken.setLastModifiedDate(new Timestamp(lastUsed.getTime()));
            entityManager.merge(sysToken);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        try {
            SysToken sysToken = entityManager.find(SysToken.class, seriesId);
            if (sysToken != null) {
                return new PersistentRememberMeToken(sysToken.getUsername(), sysToken.getId(), sysToken.getToken(), sysToken.getLastModifiedDate());
            }
        } catch (EmptyResultDataAccessException var3) {
            log.debug("Querying token for series '{}' returned no results.", seriesId);
        } catch (IncorrectResultSizeDataAccessException var4) {
            log.error("Querying token for series '{}' returned more than one value. Series should be unique", seriesId, var4);
        } catch (DataAccessException var5) {
            log.error("Failed to load token for series '{}'", seriesId, var5);
        }

        return null;
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SysToken> criteriaQuery = criteriaBuilder.createQuery(SysToken.class);
        Root<SysToken> root = criteriaQuery.from(SysToken.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("username"), username));
        List<SysToken> list = entityManager.createQuery(criteriaQuery).getResultList();
        if (list.size() > 0) {
            SysToken sysToken = list.get(0);
            entityManager.remove(sysToken);
        }
    }
}
