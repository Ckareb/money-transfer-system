package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.auth.service.AccessService;
import com.course.money_transfer_system.auth.service.UserAccountService;
import com.course.money_transfer_system.exception.AccessDeniedException;
import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.exception.IncorrectParamException;
import com.course.money_transfer_system.transfer.dto.AccountModifyAccessDto;
import com.course.money_transfer_system.transfer.dto.AccountUseAccessDto;
import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.mapper.AccountMapper;
import com.course.money_transfer_system.transfer.model.Account;
import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.course.money_transfer_system.transfer.model.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.CurrencyType;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserAccountService userAccountService;

    public AccountService(AccountRepository accountRepository,
                          UserAccountService userAccountService) {
        this.accountRepository = accountRepository;
        this.userAccountService = userAccountService;
    }

    /**
     * Получает данные банковских счетов
     * @param id Id аккаунта
     * @return банковские счета
     */
    public List<AccountDto> getAccounts(Long id) {
        if (!canUseAccounts(id) || canModifyAccounts())
            throw new AccessDeniedException();

        if (!checkUserAccount(id)) {
            throw new EntityNotFoundException("Данного пользователя не существует", id.toString());
        }

        return accountRepository.getAccounts(id);
    }

    /**
     * Получает данных банковского счета
     * @param id Id банковского счета
     * @return банковский счет
     */
    public AccountDto getAccountDto(Long id) {
        if (!canUseAccounts(id))
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
        if (!canModifyAccounts())
            throw new AccessDeniedException();

        checkDto(dto);
        Account account = AccountMapper.INSTANCE.toAccount(dto);
        //Заполняемые поля
        account.setId(null);
        account.setCreatedAt(LocalDateTime.now());

        return AccountMapper.INSTANCE.toAccountDto(accountRepository.createAccount(account));
    }

    /**
     * Изменяет данные банковского счета
     * @param dto Id банковского счета
     * @return банковский счет
     */
    @Transactional
    public AccountDto changeAccount(AccountDto dto) {
        if (!canModifyAccounts())
            throw new AccessDeniedException();
        checkDtoBeforeChange(dto.getId());

        checkDto(dto);
        Account account = AccountMapper.INSTANCE.toAccount(dto);
        account.setCreatedAt(LocalDateTime.now());
        return AccountMapper.INSTANCE.toAccountDto(accountRepository.changeAccount(account));
    }

    /**
     * Удаляет банковский счет
     * @param id Id банковского счета
     * @return статус удаления
     */
    @Transactional
    public ResponseEntity<ResponseInfo> deleteAccount(Long id) {
        if (!canModifyAccounts())
            throw new AccessDeniedException();

        checkDtoBeforeChange(id);

        int result = accountRepository.deleteAccount(id);
        if (result > 0) {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Данная позиция успешная удалена",
                            LocalDateTime.now(),
                            TransactionStatus.SUCCESS.getDescription(),
                            TransactionStatus.SUCCESS.getName().toUpperCase()
                    ),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ResponseInfo(
                            "Произошла ошибка удаления ",
                            LocalDateTime.now(),
                            TransactionStatus.FAILED.getDescription(),
                            TransactionStatus.FAILED.getName().toUpperCase()
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
        return Arrays.stream(TransactionType.values()).map(TransactionType::getEnumDto).toList();
    }

    /**
     * Получает тип валюты
     * @return тип валюты
     */
    public List<EnumDto> getCurrencyType() {
        return Arrays.stream(CurrencyType.values()).map(CurrencyType::getEnumDto).toList();
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
        return accountRepository.existUser(id);
    }

    /**
     * Получает id пользователя
     * @param accountNumber номер аккаунта
     * @return id пользователя
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

        for (CurrencyType type : CurrencyType.values()) {
            if (dto.getCurrency().equals(type.getCurrencyTypeId())) {
                existsCurrencyType = true;
                break;
            }
        }

        if (dto.getAccountNumber() == null) {
            throw new EntityNotFoundException("Номер счета введен не верно");
        }

        if (accountRepository.existAccountNumber(dto.getAccountNumber())) {
            throw new IncorrectParamException("Номер счета уже существует", dto.getAccountNumber(), "accountNumber");
        }

        if (dto.getCurrency() == null || !existsCurrencyType)
            throw new IncorrectParamException("Данная валюта не поддерживается", dto.getCurrency() != null ? dto.getCurrency().toString() : null, "currency");
    }

    private void checkDtoBeforeChange(Long id) {
        if (checkExistAccount(id)) {
            throw new EntityNotFoundException("Данного счета не существует", id.toString());
        }

        if (!checkBalance(accountRepository.getAccount(id).getBalance())) {
            throw new IncorrectParamException("Балан перед удалением/изменением должен быть равен 0");
        }
    }

    public boolean canTransaction(String numberFrom){
        return canUseAccounts(getAccountId(numberFrom));
    }

    private boolean canUseAccounts(Long id){
        return isUser(accountRepository.getAccount(id));
    }

    private boolean isUser(Account account) {
        System.out.println(userAccountService.findUserId());
        return account != null && AccessService.isUser() &&
                account.getUserAccountId() != null &&
                account.getUserAccountId().equals(userAccountService.findUserId().getId());
    }

    private boolean canModifyAccounts(){
        return AccessService.isAdmin();
    }

    public AccountModifyAccessDto getModifyAccess() {
        AccountModifyAccessDto access = new AccountModifyAccessDto();

        access.setReadList(canModifyAccounts());
        access.setChange(canModifyAccounts());
        access.setDelete(canModifyAccounts());

        return access;
    }

    public AccountUseAccessDto getUseAccess(Long id) {
        AccountUseAccessDto access = new AccountUseAccessDto();

        access.setRead(canUseAccounts(id));
        access.setTransfer(canUseAccounts(id));

        return access;
    }
}
