package com.gft.dicionario.dicionario_api.repositories;

import com.gft.dicionario.dicionario_api.entities.Role;
import com.gft.dicionario.dicionario_api.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Role adminRole;
    private Role basicRole;

    @BeforeEach
    void setup() {
        //cria e salva as roles
        adminRole = new Role("ADMIN");
        basicRole = new Role("BASIC");
        roleRepository.save(adminRole);
        roleRepository.save(basicRole);

        Set<Role> roles = new HashSet<>();
        roles.add(basicRole);

        user = new User("william", "123456");
        user.setRoles(roles);

        userRepository.save(user);
    }


    // ------------  CAMINHOS FELIZES ------------------
    @Test
    @DisplayName("Deve salvar e recuperar um usuário por ID")
    void saveAndFindById() {
        Optional<User> foundUser = userRepository.findById(user.getUserId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("william");
        assertThat(foundUser.get().getRoles())
                .extracting(Role::getRoleName)
                .containsExactly("BASIC");
    }

    @Test
    @DisplayName("Deletar usuário existente remove do banco")
    void deleteUser_existing() {
        userRepository.delete(user);
        assertThat(userRepository.findById(user.getUserId())).isNotPresent();
    }


    // ---------- CAMINHOS ALTERNATIVOS ---------
    @Test
    @DisplayName("Buscar por ID inexistente retorna vazio")
    void findById_noMatch() {
        Optional<User> found = userRepository.findById(UUID.randomUUID());
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("Deletar usuário inexistente não lança exceção")
    void deleteUser_nonExisting() {
        userRepository.deleteById(UUID.randomUUID());
    }
}
