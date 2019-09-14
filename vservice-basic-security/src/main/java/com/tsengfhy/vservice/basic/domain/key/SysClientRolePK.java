package com.tsengfhy.vservice.basic.domain.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SysClientRolePK implements Serializable {

    private static final long serialVersionUID = -992908330429888358L;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;
}
