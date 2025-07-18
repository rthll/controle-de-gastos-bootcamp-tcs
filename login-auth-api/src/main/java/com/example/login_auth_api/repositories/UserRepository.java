package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.User.User;
import com.example.login_auth_api.domain.User.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM sp_find_user_by_email(:email)", nativeQuery = true)
    Optional<User> findByEmailWithProcedure(@Param("email") String email);

    @Transactional
    @Query(value = "SELECT sp_register_user(:name, :email, :password, :role)", nativeQuery = true)
    Long registerUserWithProcedure(@Param("name") String name,
                                   @Param("email") String email,
                                   @Param("password") String password,
                                   @Param("role") String role);


    @Transactional
    @Query(value = "SELECT sp_update_user(:email, :name, :password)", nativeQuery = true)
    void updateUserWithProcedure(
            @Param("email") String email,
            @Param("name") String name,
            @Param("password") String password
    );

    @Modifying
    @Transactional
    @Query(value = "SELECT sp_switch_user_role(:email, :newRole)", nativeQuery = true)
    void switchUserRoleWithProcedure(
            @Param("email") String email,
            @Param("newRole") String newRole
    );

    @Query(value = "SELECT sp_user_exists(:email)", nativeQuery = true)
    Boolean userExistsWithProcedure(@Param("email") String email);

    @Transactional
    @Query(value = "SELECT sp_update_user_password(:email, :newPassword)", nativeQuery = true)
    void updatePasswordWithProcedure(
            @Param("email") String email,
            @Param("newPassword") String newPassword
    );

    @Query(value = "SELECT sp_get_user_role(:email)", nativeQuery = true)
    String getUserRoleWithProcedure(@Param("email") String email);
}