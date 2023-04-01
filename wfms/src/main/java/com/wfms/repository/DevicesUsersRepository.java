package com.wfms.repository;

import com.wfms.entity.DeviceUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicesUsersRepository extends JpaRepository<DeviceUsers,String> {
    @Query(value = "SELECT * FROM device_users where device_id = :deviceId",nativeQuery = true)
    Optional<DeviceUsers> findByDeviceId(@Param("deviceId") String deviceId);
    @Query(value = "SELECT * FROM device_users where user_id = :userId",nativeQuery = true)
    List<DeviceUsers> findDeviceByUserId(@Param("userId")Long userId);
    @Query(value = "SELECT * FROM device_users where firebase_registration_token = :token",nativeQuery = true)
    DeviceUsers findByToken(@Param("token") String deviceId);
}
