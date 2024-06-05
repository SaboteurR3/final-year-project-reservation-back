package ge.project.security.user.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    INTERNAL("შიდა"),
    EXTERNAL("გარე");

    private final String nameKa;
}
