package com.finance.sugarmarket.auth.repo;

import com.finance.sugarmarket.auth.model.MFUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MFUserRepo extends JpaRepository<MFUser, Long> {
    public MFUser findByUsername(String username);

    public MFUser findByEmail(String email);

    @Query("SELECT user FROM MFUser user WHERE user.email=:email AND user.isActive=true")
    public MFUser findByEmailAndISActive(@Param("email") String email);

    @Query("SELECT user FROM MFUser user WHERE user.username=:username AND user.isActive=true")
    public MFUser findBYUsernameAndISActive(@Param("username") String username);

    @Query("SELECT user FROM MFUser user WHERE user.isActive=true")
    public List<MFUser> findActiveUserList();

    boolean existsByUsernameAndIsActive(String username, boolean isActive);

    boolean existsByEmailAndIsActive(String email, boolean isActive);
}