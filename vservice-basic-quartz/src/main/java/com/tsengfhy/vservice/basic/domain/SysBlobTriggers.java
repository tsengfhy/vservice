package com.tsengfhy.vservice.basic.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "sys_blob_triggers", schema = "sys",
        indexes = {
                @Index(name = "IDX_BT_T_G", columnList = "sched_name, trigger_name, trigger_group")
        }
)
public class SysBlobTriggers implements Serializable {

    private static final long serialVersionUID = 905890976525498865L;

    @Id
    @OneToOne(cascade = CascadeType.REFRESH, optional = false)
    @JoinColumns({
            @JoinColumn(name = "sched_name", referencedColumnName = "sched_name", nullable = false),
            @JoinColumn(name = "trigger_name", referencedColumnName = "trigger_name", nullable = false),
            @JoinColumn(name = "trigger_group", referencedColumnName = "trigger_group", nullable = false)
    })
    private SysTriggers sysTriggers;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "blob_data", columnDefinition = "BLOB")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] blobData;
}
