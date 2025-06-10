package com.finance.sugarmarket.auth.repo;

import com.finance.sugarmarket.auth.model.MapUserProperties;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MapUserPropertiesRepo extends JpaRepository<MapUserProperties, Long> {

    @Query("SELECT m.value FROM MapUserProperties m WHERE m.user.id=:userId AND m.userProperties.name=:name")
    public String fetchValueByPropertyName(@Param("name") String name, @Param("userId") Long userId);

}
