package ge.project.security.user.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("აქტიური"),
    DISABLED("დაბლოკილი");

    private final String nameKa;
}
