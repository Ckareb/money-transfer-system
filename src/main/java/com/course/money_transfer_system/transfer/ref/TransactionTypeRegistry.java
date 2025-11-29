package com.course.money_transfer_system.transfer.ref;

import com.course.money_transfer_system.transfer.model.TypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionTypeRegistry {

    private final ConcurrentHashMap<String, TypeInfo> registry;

    public TransactionTypeRegistry(Map<String, TypeInfo> map) {
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
            throw new IllegalArgumentException("Неизвестный тип транзакции: " + code);
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
