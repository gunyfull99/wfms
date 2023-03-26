package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "device_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeviceUsers {
    @Id
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "user_id")
    private Long userId;
}
