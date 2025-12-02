package com.course.money_transfer_system.user.config;


import com.course.money_transfer_system.transfer.model.TypeInfo;
import com.course.money_transfer_system.user.ref.RoleRegistry;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static jooq.auth.Tables.ROLE;


@Configuration
@RequiredArgsConstructor
public class RoleConfig {

    @Autowired
    private final DSLContext dsl;

    @Bean
    public RoleRegistry roleRegistry() {
        try {

            Map<String, TypeInfo> map = dsl.selectFrom(ROLE)
                    .fetch()
                    .stream()
                    .collect(Collectors.toMap(
                            r -> r.getSysName().toUpperCase(),
                            r -> new TypeInfo(
                                    r.getId(),
                                    r.getSysName(),
                                    null,
                                    r.getDescription()
                            ),
                            (a, b) -> a,
                            ConcurrentHashMap::new
                    ));

            if (map.isEmpty()) {
                throw new IllegalStateException("role table is EMPTY — startup aborted");
            }

            return new RoleRegistry(map);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load roles", e);
        }
    }
}
