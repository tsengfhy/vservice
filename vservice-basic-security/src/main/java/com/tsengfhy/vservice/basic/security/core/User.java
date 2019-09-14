package com.tsengfhy.vservice.basic.security.core;

import com.tsengfhy.vservice.basic.dto.security.UserDto;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class User extends UserDto implements UserDetails {

    @Setter
    private boolean accountExpired = false;

    @Setter
    private boolean credentialsExpired = false;

    private Collection<GrantedAuthority> authorities;

    public User(UserDto userDto, Collection<GrantedAuthority> authorities) {
        BeanUtils.copyProperties(userDto, this);
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return StringUtils.isBlank(getLocked()) || "0".equals(getLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.isNotBlank(getEnabled()) && "1".equals(getEnabled());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("Username", this.getUsername())
                .append("Mail", this.getMail())
                .append("Phone", this.getPhone())
                .append("Token", this.getToken())
                .append("Password", "[PROTECTED]")
                .append("Enabled", this.getEnabled())
                .append("AccountNonLocked", this.isAccountNonLocked())
                .append("AccountNonExpired", this.isAccountNonExpired())
                .append("CredentialsNonExpired", this.isCredentialsNonExpired())
                .append("Authorities", this.getAuthorities().isEmpty() ? "Not granted any authorities" : this.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User that = (User) o;
            return new EqualsBuilder()
                    .append(this.getUsername(), that.getUsername())
                    .append(this.getMail(), that.getMail())
                    .append(this.getPhone(), that.getPhone())
                    .append(this.getToken(), that.getToken())
                    .isEquals();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 59)
                .append(this.getUsername())
                .append(this.getMail())
                .append(this.getPhone())
                .append(this.getToken())
                .hashCode();
    }
}
