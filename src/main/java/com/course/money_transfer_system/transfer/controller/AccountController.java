package com.course.money_transfer_system.transfer.controller;

import com.course.money_transfer_system.auth.model.UserDetails;
import com.course.money_transfer_system.transfer.dto.*;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.transfer.service.AccountService;
import com.course.money_transfer_system.transfer.service.TransactionHistoryService;
import com.course.money_transfer_system.transfer.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Операции со счетами")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransactionHistoryService transactionHistoryService;

    public AccountController(AccountService accountService,
                             TransactionService transactionService,
                             TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @Operation(summary = "Просмотр всех счетов аккаунта")
    @GetMapping(path = "/me")
    public List<AccountDto> getAccounts(@AuthenticationPrincipal UserDetails user) {
        Long userId = user.getUserId();
        return  accountService.getAccounts(userId);
    }

    @Operation(summary = "Просмотр данных счета")
    @GetMapping(path = "/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return  accountService.getAccountDto(id);
    }

    @Operation(summary = "Создание счета")
    @PostMapping(path = "/create")
    public AccountDto createAccount(@RequestBody AccountDto dto) {
        return accountService.createAccount(dto);
    }

    @Operation(summary = "Изменение данных счета")
    @PutMapping(path = "/change")
    public AccountDto changeAccount(@RequestBody AccountDto dto) {
        return accountService.changeAccount(dto);
    }

    @Operation(summary = "Просмотр доступных типов операции")
    @GetMapping(path = "/transfer-type")
    public List<EnumDto> getTransactionType() {
        return  accountService.getTransactionType();
    }

    @Operation(summary = "Просмотр доступных типов валюты")
    @GetMapping(path = "/currency-type")
    public List<EnumDto> getCurrencyType() {
        return  accountService.getCurrencyType();
    }

    @Operation(summary = "Просмотр доступных типов счетов")
    @GetMapping(path = "/account-type")
    public List<EnumDto> getAccountType() {
        return  accountService.getAccountType();
    }

    @Operation(summary = "Закрытие счета")
    @PutMapping(path = "/close/{id}")
    public ResponseEntity<ResponseInfo> closeAccount(@PathVariable Long id) {
        return accountService.closeAccount(id);
    }

    @Operation(summary = "Изменение баланса счета")
    @PutMapping(path = "/change/balance")
    public ResponseEntity<ResponseInfo> changeBalance(@RequestBody TransactionDto dto) {
        return transactionService.transaction(dto);
    }

    @Operation(summary = "Список истории транзакций")
    @GetMapping("/{id}/history-transaction")
    public Page<TransactionHistoryDto> getTransactionHistoryPages(@PathVariable Long id, @PageableDefault(size = 20) Pageable pageable) {
        return transactionHistoryService.getTransactionHistoryPages(id, pageable);
    }

    @Operation(summary = "Доступ к операциям без id")
    @GetMapping(path = "/access")
    public AccountAccessDto getModifyAccess() {
        return accountService.getAccess();
    }

    @Operation(summary = "Доступ к операциям с id")
    @GetMapping(path = "/{id}/access")
    public AccountAccessWithIdDto getUseAccess(@PathVariable Long id) {
        return accountService.getAccessWithId(id);
    }
}
