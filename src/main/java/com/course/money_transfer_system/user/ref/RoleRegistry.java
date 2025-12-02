package com.course.money_transfer_system.user.ref;

import com.course.money_transfer_system.transfer.model.TypeInfo;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoleRegistry {

    private final ConcurrentHashMap<String, TypeInfo> registry;

    public RoleRegistry(Map<String, TypeInfo> map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException("Registry map cannot be null or empty");
        }
        this.registry = new ConcurrentHashMap<>(map);
    }

    /**
     * Получение типа транзакции по коду
     */
    public TypeInfo get(String code) {
        TypeInfo type = registry.get(code.toUpperCase());
        if (type == null) {
            throw new IllegalArgumentException("Неизвестный статус аккаунт: " + code);
        }
        return type;
    }

    /**
     * Все типы транзакций (аналог values() у enum)
     */
    public Collection<TypeInfo> values() {
        return registry.values();
    }

    /**
     * Получение всех типов как Map
     */
    public Map<String, TypeInfo> getAll() {
        return registry;
    }
}
