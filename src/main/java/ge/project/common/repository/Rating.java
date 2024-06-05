package ge.project.common.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
@Getter
public enum Rating {
    ONE_STAR(1),
    TWO_STARS(2),
    THREE_STARS(3),
    FOUR_STARS(4),
    FIVE_STARS(5),
    SIX_STARS(6),
    SEVEN_STARS(7),
    EIGHT_STARS(8),
    NINE_STARS(9),
    TEN_STARS(10);

    private final int value;

    public String getCode() {
        return this.name();
    }
}
