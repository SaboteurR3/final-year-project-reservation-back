package ge.project.common.paginationandsort;

import org.springframework.data.domain.Page;

import java.util.Collection;

public record PageView<Clazz>(long totalRows,
                              Collection<Clazz> results) {

    public static <Clazz> PageView<Clazz> of(Page<Clazz> results) {
        return new PageView<>(results.getTotalElements(), results.getContent());
    }

}
