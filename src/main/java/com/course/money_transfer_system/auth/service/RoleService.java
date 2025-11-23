package com.course.money_transfer_system.auth.service;

import com.course.money_transfer_system.auth.model.Role;
import com.course.money_transfer_system.auth.model.UserAccount;
import com.course.money_transfer_system.auth.repository.RoleRepository;
import com.course.money_transfer_system.auth.repository.UserAccountRepository;
import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.exception.IncorrectParamException;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserAccountRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserAccountRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public String getUserRole(String username) {
        UserAccount user = userRepository.findByUsername(username);
        Role role = roleRepository.findById(user.getRoleId());
        checkRole(role);
        return role.getSysName();
    }

    private void checkRole(Role role){
        if (role == null || role.getSysName() == null) {
            throw new EntityNotFoundException("Пользователь с данным логином не найден");
        }

        if (!role.isActual())
            throw new IncorrectParamException("Данная роль больше не поддерживается");
    }

}
