package ge.project.common.paginationandsort;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record PageAndSortCriteria(@Min(1) Integer page,
                                  @Min(1) Integer size,
                                  List<@Pattern(regexp = "^[a-zA-Z0-9_.]+:(asc|desc)$") String> orderBy) {
    public Pageable build(String defaultOrderByValue) {
        return build(defaultOrderByValue, Collections.emptyMap());
    }

    public Pageable build(String defaultOrderByValue, Map<String, String> orderByColumnsMap) {
        Sort sort = SortCriteria.buildOrderBy(defaultOrderByValue, orderByColumnsMap, orderBy);
        return PageRequest.of(getPage() - 1, getSize(), sort);
    }

    public Integer getPage() {
        return page != null ? page : 1;
    }

    public Integer getSize() {
        return size != null ? size : Integer.MAX_VALUE;
    }
}
