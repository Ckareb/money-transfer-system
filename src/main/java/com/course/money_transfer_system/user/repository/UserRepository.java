package com.course.money_transfer_system.user.repository;

import com.course.money_transfer_system.user.dto.UserDataDto;
import com.course.money_transfer_system.user.dto.UserDto;
import com.course.money_transfer_system.user.ref.RoleType;
import jakarta.annotation.PostConstruct;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jooq.auth.Tables.*;
import static jooq.money_transfer.Tables.*;
import static org.apache.kafka.common.requests.DeleteAclsResponse.log;
import static org.jooq.impl.DSL.*;

@Repository
public class UserRepository {
    @Autowired
    private DSLContext dsl;

    @PostConstruct
    private void fillRoles() {
        dsl.selectFrom(ROLE).where(ROLE.SYS_NAME.isNotNull().and(ROLE.IS_ACTUAL.isTrue()))
                .fetch().forEach(r -> RoleType.fill(r.getId(), r.getSysName(), r.getDescription()));
        log.info("Role Type add in project");
    }


    public Long countUsers() {
        return dsl.selectCount()
                .from(USER_ACCOUNT)
                .fetchOne(0, Long.class);
    }

    public List<UserDto> getUsers(int offset, int limit){
        return dsl.select(
                        USER_ACCOUNT.ID,
                        USER_ACCOUNT.USERNAME,
                        multiset(
                                select(
                                        USER_DATA.FULL_NAME
                                )
                                .from(USER_DATA)
                                        .where(USER_DATA.USER_ID.eq(USER_ACCOUNT.ID))
                        ).convertFrom(r -> r.getFirst().into(UserDataDto.class))
                                .as("userDataDto")

                )
                .from(USER_ACCOUNT)
                .where(USER_ACCOUNT.ROLE_ID.eq(RoleType.USER.getRoleTypeId()))
                .offset(offset).limit(limit)
                .fetchInto(UserDto.class);
    }

    public UserDto getUserDto(Long id){
        return dsl.select(
                        USER_ACCOUNT.ID,
                        USER_ACCOUNT.USERNAME,
                        multiset(
                                selectFrom(USER_DATA)
                                        .where(USER_DATA.USER_ID.eq(USER_ACCOUNT.ID))
                        ).convertFrom(r -> r.getFirst().into(UserDataDto.class))
                                .as("userDataDto")

                )
                .from(USER_ACCOUNT)
                .where(USER_ACCOUNT.ID.eq(id))
                .and(USER_ACCOUNT.ROLE_ID.eq(RoleType.USER.getRoleTypeId()))
                .fetchOneInto(UserDto.class);
    }

    public Long createUser(UserDto dto) {

        Long userId = dsl.insertInto(USER_ACCOUNT)
                .set(USER_ACCOUNT.USERNAME, dto.getUsername())
                .set(USER_ACCOUNT.PASSWORD, dto.getPassword())
                .set(USER_ACCOUNT.ROLE_ID, dto.getRoleId())
                .set(USER_ACCOUNT.ENABLED, true)
                .returning(USER_ACCOUNT.ID)
                .fetchOne()
                .getId();

        UserDataDto data = dto.getUserDataDto();

        dsl.insertInto(USER_DATA)
                .set(USER_DATA.USER_ID, userId)
                .set(USER_DATA.FULL_NAME, data.getFullName())
                .set(USER_DATA.BIRTH_DATE, data.getBirthDate())
                .set(USER_DATA.COUNTRY, data.getCountry())
                .set(USER_DATA.NUMBER_PHONE, data.getNumberPhone())
                .set(USER_DATA.PASSPORT_SERIES, data.getPassportSeries())
                .set(USER_DATA.PASSPORT_NUMBER, data.getPassportNumber())
                .set(USER_DATA.PASSPORT_ISSUED_BY, data.getPassportIssuedBy())
                .set(USER_DATA.PASSPORT_ISSUE_DATE, data.getPassportIssueDate())
                .execute();

        return userId;
    }

    public int changeUserData(UserDataDto dataDto){
        return dsl.update(USER_DATA)
                .set(USER_DATA.FULL_NAME, dataDto.getFullName())
                .set(USER_DATA.BIRTH_DATE, dataDto.getBirthDate())
                .set(USER_DATA.COUNTRY, dataDto.getCountry())
                .set(USER_DATA.NUMBER_PHONE, dataDto.getNumberPhone())
                .set(USER_DATA.PASSPORT_SERIES, dataDto.getPassportSeries())
                .set(USER_DATA.PASSPORT_NUMBER, dataDto.getPassportNumber())
                .set(USER_DATA.PASSPORT_ISSUED_BY, dataDto.getPassportIssuedBy())
                .set(USER_DATA.PASSPORT_ISSUE_DATE, dataDto.getPassportIssueDate())
                .where(USER_DATA.ID.eq(dataDto.getId()))
                .execute();
    }

    public int disableUser(Long id){
        return dsl.update(USER_ACCOUNT)
                .set(USER_ACCOUNT.ENABLED, false)
                .where(USER_ACCOUNT.ID.eq(id))
                .and(USER_ACCOUNT.ROLE_ID.eq(RoleType.USER.getRoleTypeId()))
                .execute();
    }
}
