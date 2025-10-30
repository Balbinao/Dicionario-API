package com.gft.dicionario.dicionario_api.config;


import com.gft.dicionario.dicionario_api.entities.Role;
import com.gft.dicionario.dicionario_api.entities.User;
import com.gft.dicionario.dicionario_api.repositories.RoleRepository;
import com.gft.dicionario.dicionario_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = roleRepository.findByRoleName("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setRoleName("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
        }

        //Cria usuário admin se não existir
        Role finalRoleAdmin = roleAdmin;
        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> System.out.println("Usuário admin já existe."),
                () -> {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode("1234"));
                    admin.setRoles(Set.of(finalRoleAdmin));
                    userRepository.save(admin);
                    System.out.println("Usuário admin criado com sucesso!");
                }
        );

        User userBasic = userRepository.findByUsername("user").orElse(null);
        if (userBasic == null) {

            Role roleBasic = roleRepository.findByRoleName("ROLE_BASIC");
            if (roleBasic == null) {
                roleBasic = new Role();
                roleBasic.setRoleName("ROLE_BASIC");
                roleRepository.save(roleBasic);
            }

            //cria o usuário
            userBasic = new User();
            userBasic.setUsername("user");
            userBasic.setPassword(passwordEncoder.encode("123"));
            userBasic.setRoles(Set.of(roleBasic));
            userRepository.save(userBasic);
            System.out.println("Usuário BASIC criado com sucesso!");
        } else {
            System.out.println("Usuário BASIC já existe.");
        }
    }
}