package com.tsengfhy.vservice.basic.security.oauth2;

import com.tsengfhy.vservice.basic.dto.security.ClientDto;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Client extends ClientDto implements ClientDetails {

    private Set<String> authorizedGrantTypes;

    private Set<String> scope;

    private Set<String> autoApproveScopes;

    private Set<String> resourceIds;

    private Set<String> registeredRedirectUri;

    private Collection<GrantedAuthority> authorities;

    @Setter
    private Map<String, Object> additionalInformation;

    @Setter
    private Integer accessTokenValiditySeconds;

    @Setter
    private Integer refreshTokenValiditySeconds;

    public Client(ClientDto clientDto, Collection<GrantedAuthority> authorities) {
        BeanUtils.copyProperties(clientDto, this);
        this.authorities = authorities;
        this.init();
    }

    private void init() {
        this.authorizedGrantTypes = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(super.getGrantTypes())) {
            for (String grantType : super.getGrantTypes().split(",")) {
                if (StringUtils.isNotBlank(grantType)) {
                    this.authorizedGrantTypes.add(grantType);
                }
            }
        }

        this.scope = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(super.getScopes())) {
            for (String scope : super.getScopes().split(",")) {
                if (StringUtils.isNotBlank(scope)) {
                    this.scope.add(scope);
                }
            }
        }

        this.autoApproveScopes = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(super.getApproveScopes())) {
            for (String approveScope : super.getApproveScopes().split(",")) {
                if (StringUtils.isNotBlank(approveScope)) {
                    this.autoApproveScopes.add(approveScope);
                }
            }
        }

        this.resourceIds = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(super.getResources())) {
            for (String resourceId : super.getResources().split(",")) {
                if (StringUtils.isNotBlank(resourceId)) {
                    this.resourceIds.add(resourceId);
                }
            }
        }

        this.registeredRedirectUri = new LinkedHashSet<>();
        if (StringUtils.isNotBlank(super.getRedirectUris())) {
            for (String registeredRedirectUri : super.getRedirectUris().split(",")) {
                if (StringUtils.isNotBlank(registeredRedirectUri)) {
                    this.registeredRedirectUri.add(registeredRedirectUri);
                }
            }
        }
    }

    @Override
    public String getClientId() {
        return String.valueOf(this.getId());
    }

    @Override
    public boolean isSecretRequired() {
        return StringUtils.isNotBlank(this.getSecret());
    }

    @Override
    public String getClientSecret() {
        return this.getSecret();
    }

    @Override
    public boolean isScoped() {
        return !this.scope.isEmpty();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if (auto.equals("true") || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(this.authorities);
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return Collections.unmodifiableMap(additionalInformation);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ClientId", this.getClientId())
                .append("ClientSecret", "[PROTECTED]")
                .append("Scopes", this.getScope().isEmpty() ? "Not have any scopes" : StringUtils.join(this.getScope()))
                .append("ResourceIds", this.getResourceIds().isEmpty() ? "Not have any resourceIds" : StringUtils.join(this.getResourceIds()))
                .append("AuthorizedGrantTypes", this.getAuthorizedGrantTypes().isEmpty() ? "Not have any grant types" : StringUtils.join(this.getAuthorizedGrantTypes()))
                .append("RegisteredRedirectUris", this.getRegisteredRedirectUri().isEmpty() ? "Not have any registered redirect uri" : StringUtils.join(this.getRegisteredRedirectUri()))
                .append("Authorities", this.getAuthorities().isEmpty() ? "Not granted any authorities" : this.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.joining(",")))
                .append("AccessTokenValiditySeconds", this.getAccessTokenValiditySeconds())
                .append("RefreshTokenValiditySeconds", this.getRefreshTokenValiditySeconds())
                .append("AdditionalInformation", this.getAdditionalInformation().isEmpty() ? "Not any additional information" : this.getAdditionalInformation())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Client)) {
            return false;
        } else {
            Client that = (Client) o;
            return new EqualsBuilder().append(this.getClientId(), that.getClientId()).isEquals();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 59)
                .append(this.getClientId())
                .hashCode();
    }
}

