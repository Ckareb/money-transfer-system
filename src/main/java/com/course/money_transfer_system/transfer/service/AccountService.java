package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.mapper.AccountMapper;
import com.course.money_transfer_system.transfer.model.Account;
import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.course.money_transfer_system.transfer.ref.CurrencyType;
import com.course.money_transfer_system.transfer.ref.TransactionType;
import com.course.money_transfer_system.transfer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Получает данные банковских счетов
     * @param id Id аккаунта
     * @return банковские счета
     */
    public List<AccountDto> getAccounts(Long id) {
        // TODO Проверка доступа
        if (!checkUserAccount(id)) {
            System.out.println("Данного пользователя не существует");
        }
        return accountRepository.getAccounts(id);
    }

    /**
     * Получает данных банковского счета
     * @param id Id банковского счета
     * @return банковский счет
     */
    public AccountDto getAccount(Long id) {
        // TODO Проверка доступа
        return accountRepository.getAccount(id);
    }

    /**
     * Создает банковский счет
     * @param dto банковского счета
     * @return банковский счет
     */
    @Transactional
    public AccountDto createAccount(AccountDto dto) {
        // TODO Проверка доступа и исключения
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
        // TODO Проверка доступа и исключения
        if (checkExistAccount(dto.getId())) {
            System.out.println("Данного договора не существует");
        }

        if (!checkBalance(getAccount(dto.getId()).getBalance())) {
            System.out.println("Балан перед изменением должен быть равен 0");
        }

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
    public ResponseEntity<String> deleteAccount(Long id) {
        // TODO Проверка доступа и исключения
        if (checkExistAccount(id)) {
            System.out.println("Данного договора не существует");
        }

        if (!checkBalance(getAccount(id).getBalance())) {
            System.out.println("Балан перед удаление должен быть равен 0");
        }

        int result = accountRepository.deleteAccount(id);
        if (result > 0) {
            return ResponseEntity.ok("Запись удалена");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запись не найдена");
        }
    }

    /**
     * Проверяет наличие обязательных данных банковского счета
     * @param dto банковской счет
     */
    private void checkDto(AccountDto dto) {
        //TODO сделать классы исключений
        if (dto.getAccountNumber() == null) {
            System.out.println("Номер счета введен не верно");
        }

        if (accountRepository.existAccountNumber(dto.getAccountNumber())) {
            System.out.println("Номер счета уже существует");
        }

        if (dto.getCurrency() == null) {
            System.out.println("Валюта счета введена не верно");
        }
    }

    /**
     * Получает типы транзакций
     * @return типы транзакций
     */
    public List<EnumDto> getTransactionType() {
        // TODO Проверка доступа
        return Arrays.stream(TransactionType.values()).map(TransactionType::getEnumDto).toList();
    }

    /**
     * Получает тип валюты
     * @return тип валюты
     */
    public List<EnumDto> getCurrencyType() {
        // TODO Проверка доступа
        return Arrays.stream(CurrencyType.values()).map(CurrencyType::getEnumDto).toList();
    }

    /**
     * Проверяет наличие банковского счета
     * @param id Id банковского счета
     * @return true/false
     */
    private boolean checkExistAccount(Long id) {
        return getAccount(id) == null;
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

}
