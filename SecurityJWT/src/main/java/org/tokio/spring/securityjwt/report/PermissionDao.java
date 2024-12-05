package org.tokio.spring.securityjwt.report;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.tokio.spring.securityjwt.domain.Permission;

@Repository
public interface PermissionDao extends CrudRepository<Permission, Long> {
}
