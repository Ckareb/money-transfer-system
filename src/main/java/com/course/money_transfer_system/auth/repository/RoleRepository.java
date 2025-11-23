package com.course.money_transfer_system.auth.repository;

import com.course.money_transfer_system.auth.model.Role;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static jooq.auth.Tables.ROLE;

@Repository
public class RoleRepository {
    @Autowired
    private DSLContext dsl;

    public Role findById(Long id) {
        return dsl.select(
                    ROLE.SYS_NAME,
                    ROLE.DESCRIPTION,
                    ROLE.IS_ACTUAL
                )
                .from(ROLE)
                .where(ROLE.ID.eq(id))
                .fetchOneInto(Role.class);
    }
}
