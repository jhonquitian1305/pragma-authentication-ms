package co.com.pragma.model.user.authentication;

import java.util.Arrays;

public enum RoleEnum {
    ADMIN(1L, "ADMIN"),
    ASESOR(2L, "ASESOR"),
    CLIENTE(3L, "CLIENTE"),;

    private final Long id;
    private final String name;

    RoleEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String fromId(Long id) {
        return Arrays.stream(values())
                .filter(r -> r.id.equals(id))
                .map(RoleEnum::name)
                .findFirst()
                .orElse("CLIENTE");
    }
}