package com.course.money_transfer_system.transfer.service;

import com.course.money_transfer_system.exception.EntityNotFoundException;
import com.course.money_transfer_system.transfer.dto.TransactionDto;
import com.course.money_transfer_system.exception.ResponseInfo;
import com.course.money_transfer_system.transfer.ref.CurrencyType;
import com.course.money_transfer_system.transfer.ref.TransactionStatus;
import com.course.money_transfer_system.transfer.ref.TransactionType;
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
    private TransactionStrategy withdrawStrategy;

    @Mock
    private TransactionStrategy depositStrategy;

    private TransactionService transactionService;

    @BeforeEach
    void setTransactionTypeId() {
        TransactionType.TRANSFER.setTransactionTypeId(1L);
        TransactionType.PAYMENT.setTransactionTypeId(2L);
        TransactionType.DEPOSIT.setTransactionTypeId(3L);
    }

    @BeforeEach
    void setCurrencyTypeId() {
        CurrencyType.RUB.setCurrencyTypeId(1L);
    }

    @BeforeEach
    void setup() {

        // Настраиваем стратегии на свои typeId
        Mockito.when(transferStrategy.getTransactionTypeId()).thenReturn(TransactionType.TRANSFER.getTransactionTypeId());
        Mockito.when(paymentStrategy.getTransactionTypeId()).thenReturn(TransactionType.PAYMENT.getTransactionTypeId());
        Mockito.when(depositStrategy.getTransactionTypeId()).thenReturn(TransactionType.DEPOSIT.getTransactionTypeId());

        // Создаем TransactionService с полным списком стратегий
        transactionService = new TransactionService(
                List.of(transferStrategy, paymentStrategy, withdrawStrategy, depositStrategy),
                transactionRepository,
                accountService
        );
    }

    @Test
    void shouldReturnResponseEntityWhenTransactionIsTransfer(){
        TransactionDto dto = new TransactionDto();
        dto.setTypeId(TransactionType.TRANSFER.getTransactionTypeId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(300.00));
        dto.setCurrencyId(CurrencyType.RUB.getCurrencyTypeId());

        Mockito.when(accountService.canTransaction(anyString())).thenReturn(true);


        ResponseEntity<ResponseInfo> expected = new ResponseEntity<>(
                new ResponseInfo(
                        "Перевод совершен успешно на счет " + dto.getNumberTo(),
                        LocalDateTime.now(),
                        TransactionStatus.SUCCESS.getDescription()
                ), HttpStatus.OK);

        Mockito.when(accountService.canTransaction(anyString())).thenReturn(true);

        Mockito.when(transferStrategy.transaction(dto)).thenReturn(expected);


        ResponseEntity<ResponseInfo> actual = transactionService.transaction(dto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected.getBody().getMessage(), actual.getBody().getMessage());

        Mockito.verify(transferStrategy, Mockito.times(1)).transaction(dto);
    }

    @Test
    void shouldReturnResponseEntityWhenTransactionIsPayment() {
        TransactionDto dto = new TransactionDto();
        dto.setTypeId(TransactionType.PAYMENT.getTransactionTypeId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(500.00));
        dto.setCurrencyId(CurrencyType.RUB.getCurrencyTypeId());

        ResponseEntity<ResponseInfo> expected = new ResponseEntity<>(
                new ResponseInfo(
                        "Оплачено успешно на сумму " + dto.getAmount(),
                        LocalDateTime.now(),
                        TransactionStatus.SUCCESS.getDescription()
                ), HttpStatus.OK);

        Mockito.when(accountService.canTransaction(anyString())).thenReturn(true);

        Mockito.when(paymentStrategy.transaction(dto)).thenReturn(expected);


        ResponseEntity<ResponseInfo> actual = transactionService.transaction(dto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected.getBody().getMessage(), actual.getBody().getMessage());

        Mockito.verify(paymentStrategy, Mockito.times(1)).transaction(dto);
    }

    @Test
    void shouldReturnResponseEntityWhenTransactionIsDeposit() {
        TransactionDto dto = new TransactionDto();
        dto.setTypeId(TransactionType.DEPOSIT.getTransactionTypeId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(1000.00));
        dto.setCurrencyId(CurrencyType.RUB.getCurrencyTypeId());

        ResponseEntity<ResponseInfo> expected = new ResponseEntity<>(
                new ResponseInfo(
                        "Счет на сумму " + dto.getAmount() + " пополнен успешно",
                        LocalDateTime.now(),
                        TransactionStatus.SUCCESS.getDescription()
                ), HttpStatus.OK);

        Mockito.when(depositStrategy.transaction(dto)).thenReturn(expected);


        ResponseEntity<ResponseInfo> actual = transactionService.transaction(dto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected.getBody().getMessage(), actual.getBody().getMessage());

        Mockito.verify(depositStrategy, Mockito.times(1)).transaction(dto);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenStrategyNotExists() {
        // Подготовка DTO с typeId, которого нет в стратегии
        TransactionDto dto = new TransactionDto();
        dto.setTypeId(TransactionType.DEPOSIT.getTransactionTypeId());
        dto.setNumberFrom("ACC10002");
        dto.setNumberTo("ACC10001");
        dto.setAmount(BigDecimal.valueOf(300.00));
        dto.setCurrencyId(CurrencyType.RUB.getCurrencyTypeId());

        // Создаем TransactionService с полным списком стратегий
        transactionService = new TransactionService(
                List.of(paymentStrategy),
                transactionRepository,
                accountService
        );

        // Проверяем выброс исключения
        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.transaction(dto)
        );

        // Проверяем сообщение исключения
        Assertions.assertEquals(
                "Стратегии с данным id не существует",
                exception.getMessage()
        );
    }
}