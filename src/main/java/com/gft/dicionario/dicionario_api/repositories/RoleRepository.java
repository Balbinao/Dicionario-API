package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName (String roleName);
}
