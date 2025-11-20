package com.course.money_transfer_system.transfer.controller;

import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.transfer.dto.EnumDto;
import com.course.money_transfer_system.transfer.model.ResponseInfo;
import com.course.money_transfer_system.transfer.service.AccountService;
import com.course.money_transfer_system.transfer.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Операции со счетом")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService,
                             TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Просмотр всех счетов аккаунта")
    @GetMapping(path = "/userId/{id}")
    public List<AccountDto> getAccounts(@PathVariable Long id) {
        return  accountService.getAccounts(id);
    }

    @Operation(summary = "Просмотр данных счета")
    @GetMapping(path = "/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return  accountService.getAccount(id);
    }

    @Operation(summary = "Создание счета")
    @PostMapping(path = "/create")
    public AccountDto createAccount(@RequestBody AccountDto dto) {
        return  accountService.createAccount(dto);
    }

    @Operation(summary = "Изменение данных счета")
    @PutMapping(path = "/change")
    public AccountDto changeAccount(@RequestBody AccountDto dto) {
        return  accountService.changeAccount(dto);
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

    @Operation(summary = "Удаление счета")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ResponseInfo> deleteAccount(@PathVariable Long id) {
        return  accountService.deleteAccount(id);
    }

    @Operation(summary = "Изменение баланса счета")
    @PutMapping(path = "/change/balance")
    public ResponseEntity<ResponseInfo> changeBalance(@RequestBody TransactionDto dto) {
        return transactionService.transaction(dto);
    }

    //TODO написать эндпоинт со списком всех историй счета
}
