package org.tokio.spring.securityjwt.report;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tokio.spring.securityjwt.domain.Role;
import org.tokio.spring.securityjwt.projection.PermissionProjection;
import org.tokio.spring.securityjwt.projection.RoleProjection;

import java.util.List;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {

    @Query("SELECT r.id, r.name AS name FROM User u JOIN u.roles r WHERE u.email = :email")
    List<RoleProjection> findRolesByEmail(@Param("email") String email);

    @Query("SELECT p.id, p.name AS name FROM Role  r JOIN r.permissions p WHERE r.id = :roleId")
    List<PermissionProjection> findPermissionByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT p.id, p.name AS name FROM Role  r JOIN r.permissions p WHERE UPPER(r.name) = UPPER( :roleName )")
    List<PermissionProjection> findPermissionByRoleName(@Param("roleName") String roleName);
}
