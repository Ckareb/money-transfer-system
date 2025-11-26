package com.course.money_transfer_system.transfer.mapper;

import com.course.money_transfer_system.transfer.dto.AccountDto;
import com.course.money_transfer_system.transfer.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "currency", source = "currencyId")
    Account toAccount(AccountDto accountDto);

    @Mapping(target = "currencyId", source = "currency")
    AccountDto toAccountDto(Account account);

}
