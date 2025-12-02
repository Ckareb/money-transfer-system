package com.course.money_transfer_system.auth.service;

import com.course.money_transfer_system.auth.model.UserAccount;
import com.course.money_transfer_system.auth.model.UserDetails;
import com.course.money_transfer_system.auth.repository.UserAccountRepository;
import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.exception.IncorrectParamException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
    private final UserAccountRepository userRepository;

    public UserAccountService(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserAccount findByUsername(String username) {
        UserAccount userAccount = userRepository.findByUsername(username);
        checkUser(userAccount);
        return userAccount;
    }

    public UserAccount findUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        return userRepository.findByUsername(userName);
    }

    private void checkUser(UserAccount user) {

        if (user == null || user.getUsername() == null || user.getPassword() == null)
            throw new EntityNotFoundException("Пользователь с данным логином не найден");

        if (!user.isEnabled())
            throw new IncorrectParamException("Пользователь с данным логином заблокирован");
    }
}
