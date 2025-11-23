package com.course.money_transfer_system.auth.repository;

import com.course.money_transfer_system.auth.model.UserAccount;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static jooq.auth.Tables.USER_ACCOUNT;

@Repository
public class UserAccountRepository {
    @Autowired
    private DSLContext dsl;

    public UserAccount findByUsername(String username) {
        return dsl.select(
                    USER_ACCOUNT.ID,
                    USER_ACCOUNT.USERNAME,
                    USER_ACCOUNT.PASSWORD,
                    USER_ACCOUNT.ENABLED,
                    USER_ACCOUNT.ROLE_ID
                )
                .from(USER_ACCOUNT)
                .where(USER_ACCOUNT.USERNAME.eq(username))
                .fetchOneInto(UserAccount.class);
    }
}
