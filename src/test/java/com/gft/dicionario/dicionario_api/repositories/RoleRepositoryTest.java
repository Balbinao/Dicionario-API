package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role basicRole;
    private Role adminRole;

    @BeforeEach
    void setup(){

        basicRole = new Role("BASIC");
        adminRole = new Role("ADMIN");

        roleRepository.save(basicRole);
        roleRepository.save(adminRole);
    }


    //-------- CAMINHOS FELIZES --------
    @Test
    @DisplayName("Salvar e recuperar por ID")
    void saveAndFindById(){
        Optional<Role> found = roleRepository.findById(basicRole.getRoleId());

        assertThat(found).isPresent();
        assertThat(found.get().getRoleName()).isEqualTo("BASIC");
    }

    @Test
    @DisplayName("Listar todas as roles retorna as inseridas")
    void findAll_returnsAll() {
        List<Role> roles = roleRepository.findAll();

        assertThat(roles).hasSize(2);
        assertThat(roles)
                .extracting(Role::getRoleName)
                .containsExactlyInAnyOrder("BASIC", "ADMIN");
    }

    @Test
    @DisplayName("Deletar role existente remove do banco")
    void deleteRole_existing() {
        roleRepository.delete(basicRole);
        assertThat(roleRepository.findById(basicRole.getRoleId())).isNotPresent();
    }

    // -----CAMINHOS ALTERNATIVOS ------
    @Test
    @DisplayName("Buscar por ID inexistente retorna vazio")
    void findById_noMatch() {
        Optional<Role> found = roleRepository.findById(999L);
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Deletar role inexistente não lança exceção")
    void deleteRole_nonExisting() {
        roleRepository.deleteById(999L);
    }
}
