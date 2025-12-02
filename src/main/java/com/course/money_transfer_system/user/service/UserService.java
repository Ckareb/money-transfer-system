package com.course.money_transfer_system.user.service;

import com.course.money_transfer_system.auth.service.AccessService;
import com.course.money_transfer_system.exception.AccessDeniedException;
import com.course.money_transfer_system.exception.IncorrectParamException;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.user.dto.UserAccessDto;
import com.course.money_transfer_system.user.dto.UserDataDto;
import com.course.money_transfer_system.user.dto.UserDto;
import com.course.money_transfer_system.user.ref.RoleRegistry;
import com.course.money_transfer_system.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRegistry roleRegistry;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRegistry roleRegistry) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRegistry = roleRegistry;
    }

    public Page<UserDto> getUsers(Pageable pageable){
        if (!canModifyUser())
            throw new AccessDeniedException();

        Long total = userRepository.countUsers();

        int offset = (int) pageable.getOffset();
        int limit = pageable.getPageSize();

        List<UserDto> pageContent = userRepository.getUsers(offset, limit);

        return new PageImpl<>(pageContent, pageable, total);
    }


    public UserDto getUserDto(Long id) {
        if (!canModifyUser())
            throw new AccessDeniedException();
        return userRepository.getUserDto(id);
    }

    /**
     * Создает банковский счет
     * @param dto банковского счета
     * @return банковский счет
     */
    @Transactional
    public ResponseEntity<ResponseInfo> createUser(UserDto dto) {
        if (!canModifyUser())
            throw new AccessDeniedException();


        checkUserDto(dto);
        checkDataUserDto(dto.getUserDataDto());

        //Заполняемые поля
        dto.setId(null);
        dto.setRoleId(roleRegistry.get("USER").getId());
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.getUserDataDto().setId(null);

        UserDto result = getUserDto(userRepository.createUser(dto));
        if (result != null) {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Аккаунт успешно создан",
                            LocalDateTime.now()
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Произошла ошибка при создании аккаунта ",
                            LocalDateTime.now()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    /**
     * Изменяет данные банковского счета
     * @param dto Id банковского счета
     * @return банковский счет
     */
    @Transactional
    public ResponseEntity<ResponseInfo> changeUserData(UserDataDto dto) {
        if (!canModifyUser())
            throw new AccessDeniedException();

        checkDataUserDto(dto);

        int result = userRepository.changeUserData(dto);
        if (result > 0) {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Данные аккаунта успешно изменены",
                            LocalDateTime.now()
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Произошла ошибка при изменении данных аккаунта ",
                            LocalDateTime.now()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    /**
     * Закрывает аккаунт
     * @param id Id банковского счета
     * @return статус удаления
     */
    @Transactional
    public ResponseEntity<ResponseInfo> closeUser(Long id) {
        if (!canModifyUser())
            throw new AccessDeniedException();

        int result = userRepository.disableUser(id);
        if (result > 0) {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Аккаунт успешно закрыт",
                            LocalDateTime.now()
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Произошла ошибка при закрытии аккаунта ",
                            LocalDateTime.now()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    private void checkUserDto(UserDto dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "username");

        if (dto.getPassword() == null || dto.getPassword().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "password");
    }

    private void checkDataUserDto(UserDataDto dto) {

        if (dto.getFullName() == null || dto.getFullName().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "fullName");

        if (dto.getBirthDate() == null)
            throw new IncorrectParamException("Поле не может быть пустым", null, "birthDate");

        if (dto.getCountry() == null || dto.getCountry().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "country");

        if (dto.getNumberPhone() == null || dto.getNumberPhone().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "numberPhone");

        if (dto.getPassportSeries() == null || dto.getPassportSeries().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "passportSeries");

        if (dto.getPassportNumber() == null || dto.getPassportNumber().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "passportNumber");

        if (dto.getPassportIssuedBy() == null || dto.getPassportIssuedBy().isBlank())
            throw new IncorrectParamException("Поле не может быть пустым", null, "passportIssuedBy");

        if (dto.getPassportIssueDate() == null)
            throw new IncorrectParamException("Поле не может быть пустым", null, "passportIssueDate");
    }

    private boolean canModifyUser(){
        return AccessService.isAdmin();
    }

    public UserAccessDto getAccess() {
        UserAccessDto dto = new UserAccessDto();

        dto.setRead(canModifyUser());
        dto.setCreate(canModifyUser());
        dto.setChange(canModifyUser());
        dto.setDelete(canModifyUser());

        return dto;
    }
}
