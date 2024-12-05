package org.tokio.spring.securityjwt.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tokio.spring.securityjwt.domain.Role;
import org.tokio.spring.securityjwt.domain.User;
import org.tokio.spring.securityjwt.projection.RoleProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,Long> {

    String GET_ROLES_USERS_BY_EMAIL = "GET_ROLES_USERS_BY_EMAIL";


    Optional<User> findUserByEmailEqualsIgnoreCase(String email) ;

    @Query("SELECT r.id, r.name AS name FROM User u JOIN u.roles r WHERE u.email = :email")
    List<RoleProjection> findRolesByEmail(@Param("email") String email);
}
