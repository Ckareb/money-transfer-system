package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.transfer.model.TypeInfo;
import com.course.money_transfer_system.transfer.ref.*;
import com.course.money_transfer_system.transfer.repository.TransactionRepository;
import com.course.money_transfer_system.transfer.strategy.TransactionStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionStrategy transferStrategy;

    @Mock
    private TransactionStrategy paymentStrategy;

    @Mock
    private TransactionStrategy depositStrategy;

    private TransactionService transactionService;
    private TransactionTypeRegistry transactionTypeRegistry;
    private CurrencyTypeRegistry currencyTypeRegistry;
    private TransactionStatusRegistry transactionStatusRegistry;

    @BeforeEach
    void setupRegistryAndService() {
        // Создаем и регистрируем типы транзакций через TransactionTypeRegistry
        TypeInfo transferType = new TypeInfo(1L, "TRANSFER", "Перевод", "Перевод средств");
        TypeInfo paymentType  = new TypeInfo(2L, "PAYMENT", "Оплата", "Оплата счета");
        TypeInfo depositType  = new TypeInfo(3L, "DEPOSIT", "Депозит", "Пополнение счета");

        Map<String, TypeInfo> transactionType = new ConcurrentHashMap<>();
        transactionType.put(transferType.getCode(), transferType);
        transactionType.put(paymentType.getCode(), paymentType);
        transactionType.put(depositType.getCode(), depositType);

        transactionTypeRegistry = new TransactionTypeRegistry(transactionType);

        TypeInfo rub = new TypeInfo(1L, "RUB", "Рубль", "Рубль");

        Map<String, TypeInfo> currencyType = new ConcurrentHashMap<>();
        currencyType.put(rub.getCode(), rub);

        currencyTypeRegistry = new CurrencyTypeRegistry(currencyType);

        TypeInfo success = new TypeInfo(1L, "SUCCESS", "Успешно", "Успешно");
        TypeInfo failed = new TypeInfo(2L, "FAILED", "Провал", "Провал");

        Map<String, TypeInfo> transactionStatus = new ConcurrentHashMap<>();
        transactionStatus.put(success.getCode(), success);

        transactionStatusRegistry = new TransactionStatusRegistry(transactionStatus);

        // Настраиваем стратегии на свои typeId
        Mockito.when(transferStrategy.getTransactionTypeId()).thenReturn(transferType.getId());
        Mockito.when(paymentStrategy.getTransactionTypeId()).thenReturn(paymentType.getId());
        Mockito.when(depositStrategy.getTransactionTypeId()).thenReturn(depositType.getId());

        // Создаем TransactionService с полным списком стратегий и реестром
        transactionService = new TransactionService(
                List.of(transferStrategy, paymentStrategy, depositStrategy),
                transactionRepository,
                accountService,
                transactionTypeRegistry,
                currencyTypeRegistry
        );
    }

    @Test
    void shouldReturnResponseEntityWhenTransactionIsTransfer() {
        TypeInfo transferType = transactionTypeRegistry.get("TRANSFER");

        TransactionDto dto = new TransactionDto();
        dto.setTypeId(transferType.getId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(300.00));
        dto.setCurrencyId(currencyTypeRegistry.get("RUB").getId());

        Mockito.when(accountService.canTransaction(anyString())).thenReturn(true);

        ResponseEntity<ResponseInfo> expected = new ResponseEntity<>(
                new ResponseInfo(
                        "Перевод совершен успешно на счет " + dto.getNumberTo(),
                        LocalDateTime.now(),
                        transactionStatusRegistry.get("SUCCESS").getDescription()
                ), HttpStatus.OK
        );

        Mockito.when(transferStrategy.transaction(dto)).thenReturn(expected);

        ResponseEntity<ResponseInfo> actual = transactionService.transaction(dto);

        Mockito.verify(transferStrategy, Mockito.times(1)).transaction(dto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected.getBody().getMessage(), actual.getBody().getMessage());
    }

    @Test
    void shouldReturnResponseEntityWhenTransactionIsPayment() {
        TypeInfo paymentType = transactionTypeRegistry.get("PAYMENT");

        TransactionDto dto = new TransactionDto();
        dto.setTypeId(paymentType.getId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(500.00));
        dto.setCurrencyId(currencyTypeRegistry.get("RUB").getId());

        ResponseEntity<ResponseInfo> expected = new ResponseEntity<>(
                new ResponseInfo(
                        "Оплачено успешно на сумму " + dto.getAmount(),
                        LocalDateTime.now(),
                        transactionStatusRegistry.get("SUCCESS").getDescription()
                ), HttpStatus.OK
        );

        Mockito.when(accountService.canTransaction(anyString())).thenReturn(true);
        Mockito.when(paymentStrategy.transaction(dto)).thenReturn(expected);

        ResponseEntity<ResponseInfo> actual = transactionService.transaction(dto);

        Mockito.verify(paymentStrategy, Mockito.times(1)).transaction(dto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected.getBody().getMessage(), actual.getBody().getMessage());
    }

    @Test
    void shouldReturnResponseEntityWhenTransactionIsDeposit() {
        TypeInfo depositType = transactionTypeRegistry.get("DEPOSIT");

        TransactionDto dto = new TransactionDto();
        dto.setTypeId(depositType.getId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(1000.00));
        dto.setCurrencyId(currencyTypeRegistry.get("RUB").getId());

        ResponseEntity<ResponseInfo> expected = new ResponseEntity<>(
                new ResponseInfo(
                        "Счет на сумму " + dto.getAmount() + " пополнен успешно",
                        LocalDateTime.now(),
                        transactionStatusRegistry.get("SUCCESS").getDescription()
                ), HttpStatus.OK
        );

        Mockito.when(depositStrategy.transaction(dto)).thenReturn(expected);

        ResponseEntity<ResponseInfo> actual = transactionService.transaction(dto);

        Mockito.verify(depositStrategy, Mockito.times(1)).transaction(dto);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected.getBody().getMessage(), actual.getBody().getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenStrategyNotExists() {
        TypeInfo depositType = transactionTypeRegistry.get("DEPOSIT");

        TransactionDto dto = new TransactionDto();
        dto.setTypeId(depositType.getId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(300.00));
        dto.setCurrencyId(currencyTypeRegistry.get("RUB").getId());

        // Создаем TransactionService без depositStrategy
        transactionService = new TransactionService(
                List.of(paymentStrategy),
                transactionRepository,
                accountService,
                transactionTypeRegistry,
                currencyTypeRegistry
        );

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.transaction(dto)
        );

        Assertions.assertEquals("Стратегии с данным id не существует", exception.getMessage());
    }
}