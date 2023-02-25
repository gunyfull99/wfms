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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_users_id")
    private Long deviceUsersId;
    @Column(name = "device_id")
    private String deviceId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "firebase_registration_token")
    private String firebaseRegistrationToken;
    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "opertation_system")
    private String operationSystem;
}
