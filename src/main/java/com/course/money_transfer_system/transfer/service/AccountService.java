package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.auth.service.AccessService;
import com.course.money_transfer_system.auth.service.UserAccountService;
import com.course.money_transfer_system.exception.AccessDeniedException;
import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.exception.IncorrectParamException;
import com.course.money_transfer_system.transfer.dto.AccountAccessDto;
import com.course.money_transfer_system.transfer.dto.AccountAccessWithIdDto;
import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.mapper.AccountMapper;
import com.course.money_transfer_system.transfer.model.Account;
import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.course.money_transfer_system.transfer.model.TypeInfo;
import com.course.money_transfer_system.exception.ResponseInfo;

import com.course.money_transfer_system.transfer.ref.*;
import com.course.money_transfer_system.transfer.repository.AccountRepository;
import com.course.money_transfer_system.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserAccountService userAccountService;
    private final UserRepository userRepository;
    private final TransactionTypeRegistry transactionTypeRegistry;
    private final CurrencyTypeRegistry currencyTypeRegistry;
    private final TransactionStatusRegistry transactionStatusRegistry;
    private final AccountTypeRegistry accountTypeRegistry;
    private final AccountStatusRegistry accountStatusRegistry;

    public AccountService(AccountRepository accountRepository,
                          UserAccountService userAccountService,
                          UserRepository userRepository,
                          TransactionTypeRegistry transactionTypeRegistry,
                          CurrencyTypeRegistry currencyTypeRegistry,
                          TransactionStatusRegistry transactionStatusRegistry,
                          AccountTypeRegistry accountTypeRegistry,
                          AccountStatusRegistry accountStatusRegistry) {
        this.accountRepository = accountRepository;
        this.userAccountService = userAccountService;
        this.userRepository = userRepository;
        this.transactionTypeRegistry = transactionTypeRegistry;
        this.currencyTypeRegistry = currencyTypeRegistry;
        this.transactionStatusRegistry = transactionStatusRegistry;
        this.accountTypeRegistry = accountTypeRegistry;
        this.accountStatusRegistry = accountStatusRegistry;
    }

    /**
     * Получает данные банковских счетов
     * @param userId Id пользователя аккаунта
     * @return банковские счета
     */
    public List<AccountDto> getAccounts(Long userId) {
        if (!canReadListAccount())
            throw new AccessDeniedException();

        if (!checkUserAccount(userId)) {
            throw new EntityNotFoundException("Данного пользователя не существует", userId.toString());
        }

        return accountRepository.getAccounts(userId);
    }

    /**
     * Получает данных банковского счета
     * @param id Id банковского счета
     * @return банковский счет
     */
    public AccountDto getAccountDto(Long id) {
        if (!canUseAccount(id))
            throw new AccessDeniedException();
        return accountRepository.getAccountDto(id);
    }

    /**
     * Создает банковский счет
     * @param dto банковского счета
     * @return банковский счет
     */
    @Transactional
    public AccountDto createAccount(AccountDto dto) {
        if (!canCreateAccount())
            throw new AccessDeniedException();

        checkDto(dto);
        Account account = AccountMapper.INSTANCE.toAccount(dto);

        //Заполняемые поля
        account.setId(null);
        account.setCreatedAt(LocalDateTime.now());
        account.setStatusId(accountStatusRegistry.get("ACTIVE").getId());

        return AccountMapper.INSTANCE.toAccountDto(accountRepository.createAccount(account));
    }

    /**
     * Изменяет данные банковского счета
     * @param dto Id банковского счета
     * @return банковский счет
     */
    @Transactional
    public AccountDto changeAccount(AccountDto dto) {
        if (!canUseAccount(dto.getId()))
            throw new AccessDeniedException();

        checkDtoBeforeChange(dto.getId());

        checkDto(dto);
        Account account = AccountMapper.INSTANCE.toAccount(dto);
        account.setCreatedAt(LocalDateTime.now());
        return AccountMapper.INSTANCE.toAccountDto(accountRepository.changeAccount(account));
    }

    /**
     * Закрывает банковский счет
     * @param id Id банковского счета
     * @return статус закрытия
     */
    @Transactional
    public ResponseEntity<ResponseInfo> closeAccount(Long id) {
        if (!canUseAccount(id))
            throw new AccessDeniedException();

        checkDtoBeforeChange(id);

        int result = accountRepository.closeAccount(id);
        if (result > 0) {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Счет успешно закрыт",
                            LocalDateTime.now(),
                            transactionStatusRegistry.get("SUCCESS").getDescription(),
                            transactionStatusRegistry.get("SUCCESS").getName().toUpperCase()
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Произошла ошибка при закрытии счета ",
                            LocalDateTime.now(),
                            transactionStatusRegistry.get("FAILED").getDescription(),
                            transactionStatusRegistry.get("FAILED").getName().toUpperCase()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    /**
     * Получает типы транзакций
     * @return типы транзакций
     */
    public List<EnumDto> getTransactionType() {
        return transactionTypeRegistry.getAll().values()
                .stream()
                .map(TypeInfo::getEnumDto)
                .toList();
    }

    /**
     * Получает тип валюты
     * @return тип валюты
     */
    public List<EnumDto> getCurrencyType() {
        return currencyTypeRegistry.getAll().values()
                .stream()
                .map(TypeInfo::getEnumDto)
                .toList();
    }

    /**
     * Получает тип счета
     * @return тип счета
     */
    public List<EnumDto> getAccountType() {
        return accountTypeRegistry.getAll().values()
                .stream()
                .map(TypeInfo::getEnumDto)
                .toList();
    }

    /**
     * Проверяет наличие банковского счета
     * @param id Id банковского счета
     * @return true/false
     */
    private boolean checkExistAccount(Long id) {
        return accountRepository.getAccount(id) == null;
    }

    /**
     * Проверяет что баланс банковского счета пуст
     * @param balance баланс банковского счета
     * @return true/false
     */
    private boolean checkBalance(BigDecimal balance) {
        return balance.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Проверяет наличие пользователя
     * @param id Id пользователя
     * @return true/false
     */
    private boolean checkUserAccount(Long id) {
        return userRepository.existUser(id);
    }

    /**
     * Получает id счета
     * @param accountNumber номер счета
     * @return id счета
     */
    protected Long getAccountId(String accountNumber){
        return accountRepository.getAccountId(accountNumber);
    }

    /**
     * Проверяет наличие обязательных данных банковского счета
     * @param dto банковской счет
     */
    private void checkDto(AccountDto dto) {
        boolean existsCurrencyType = false;

        for (TypeInfo type : currencyTypeRegistry.values()) {
            if (dto.getCurrencyId().equals(type.getId())) {
                existsCurrencyType = true;
                break;
            }
        }

        boolean existsAccountType = false;

        for (TypeInfo type : accountTypeRegistry.values()) {
            if (dto.getTypeId().equals(type.getId())) {
                existsAccountType = true;
                break;
            }
        }

        if (dto.getAccountNumber() == null) {
            throw new EntityNotFoundException("Номер счета введен не верно");
        }

        if (accountRepository.existAccountNumber(dto.getAccountNumber())) {
            throw new IncorrectParamException("Номер счета уже существует", dto.getAccountNumber(), "accountNumber");
        }

        if (dto.getCurrencyId() == null || !existsCurrencyType)
            throw new IncorrectParamException("Данная валюта не поддерживается", dto.getCurrencyId() != null ? dto.getCurrencyId().toString() : null, "currency");

        if (dto.getTypeId() == null || !existsAccountType)
            throw new IncorrectParamException("Данный тип счета не поддерживается", dto.getTypeId() != null ? dto.getTypeId().toString() : null, "typeId");
    }

    private void checkDtoBeforeChange(Long id) {
        if (checkExistAccount(id)) {
            throw new EntityNotFoundException("Данного счета не существует", id.toString());
        }

        if (!checkBalance(accountRepository.getAccount(id).getBalance())) {
            throw new IncorrectParamException("Балан счета перед удалением/изменением должен быть равен 0");
        }
    }

    public boolean canTransaction(String numberFrom){
        return canUseAccount(getAccountId(numberFrom));
    }

    private boolean canUseAccount(Long id){
        return isUser(accountRepository.getAccount(id));
    }

    private boolean isUser(Account account) {
        return account != null && AccessService.isUser() &&
                account.getUserAccountId() != null &&
                account.getUserAccountId().equals(userAccountService.findUserId().getId());
    }

    private boolean canCreateAccount(){
        return AccessService.isUser();
    }

    private boolean canReadListAccount(){
        return AccessService.isUser() || AccessService.isAdmin();
    }

    public AccountAccessDto getAccess() {
        AccountAccessDto dto = new AccountAccessDto();

        dto.setReadList(canReadListAccount());
        dto.setCreate(canCreateAccount());

        return dto;
    }

    public AccountAccessWithIdDto getAccessWithId(Long id) {
        AccountAccessWithIdDto dto = new AccountAccessWithIdDto();

        dto.setRead(canUseAccount(id));
        dto.setTransfer(canUseAccount(id));
        dto.setChange(canUseAccount(id));
        dto.setDelete(canUseAccount(id));

        return dto;
    }
}
