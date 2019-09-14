package com.tsengfhy.vservice.basic.security.oauth2;

import com.tsengfhy.vservice.basic.domain.SysClient;
import com.tsengfhy.vservice.basic.dto.security.ClientDto;
import com.tsengfhy.vservice.basic.properties.Properties;
import com.tsengfhy.vservice.basic.repository.SysClientRepository;
import com.tsengfhy.vservice.basic.repository.SysClientRoleRepository;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.List;
import java.util.stream.Collectors;

public class ClientService implements ClientDetailsService {

    @Autowired
    private Properties properties;

    @Autowired
    private SysClientRepository sysClientRepository;

    @Autowired
    private SysClientRoleRepository sysClientRoleRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        SysClient sysClient = sysClientRepository.findById(Long.valueOf(clientId)).orElseThrow(() -> new UsernameNotFoundException(MessageSourceUtils.getMessage("Security.noClient", new Object[]{clientId}, "No client with key: " + clientId)));

        if (StringUtils.isNotBlank(sysClient.getLocked()) && !"0".equals(sysClient.getLocked())) {
            throw new LockedException(MessageSourceUtils.getMessage("Security.clientLocked", "Client is locked"));
        }

        if (!"1".equals(sysClient.getEnabled())) {
            throw new DisabledException(MessageSourceUtils.getMessage("Security.clientDisabled", "Client is disabled"));
        }

        ClientDto clientDto = new ClientDto();
        BeanUtils.copyProperties(sysClient, clientDto);
        clientDto.getRoles().addAll(sysClientRoleRepository.findAuthorities(clientDto.getId()));

        List<GrantedAuthority> authorities = clientDto.getRoles().stream().map(role -> new SimpleGrantedAuthority(String.valueOf(role))).collect(Collectors.toList());

        Client client = new Client(clientDto, authorities);
        client.setAccessTokenValiditySeconds(properties.getSecurity().getOauth2().getAuthorization().getAccessTokenValiditySeconds());
        client.setRefreshTokenValiditySeconds(properties.getSecurity().getOauth2().getAuthorization().getRefreshTokenValiditySeconds());

        return client;
    }
}
