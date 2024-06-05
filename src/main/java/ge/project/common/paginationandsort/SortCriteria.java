package ge.project.common.paginationandsort;

import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record SortCriteria(List<@Pattern(regexp = "^[a-zA-Z0-9_]+:(asc|desc)$") String> orderBy) {

    public Pageable build(String defaultOrderByValue, Map<String, String> mappedColumns) {
        Sort sort = buildOrderBy(defaultOrderByValue, mappedColumns, orderBy);
        return PageRequest.of(0, Integer.MAX_VALUE, sort);
    }

    public Pageable build(String defaultOrderByValue) {
        Sort sort = buildOrderBy(defaultOrderByValue, orderBy);
        return PageRequest.of(0, Integer.MAX_VALUE, sort);
    }

    static Sort buildOrderBy(String defaultOrderByValue, List<String> orderBy) {
        return buildOrderBy(defaultOrderByValue, Collections.emptyMap(), orderBy);
    }

    static Sort buildOrderBy(String defaultOrderByValue, Map<String, String> orderByColumnsMap, List<String> orderBy) {
        Sort sort = Sort.unsorted();
        if (orderBy == null || orderBy.isEmpty()) {
            if (defaultOrderByValue != null) {
                if (defaultOrderByValue.contains(":")) {
                    String[] split = defaultOrderByValue.split(":");
                    sort = Sort.by("desc".equals(split[1]) ? Sort.Direction.DESC : Sort.Direction.ASC, split[0]);
                } else {
                    sort = Sort.by(defaultOrderByValue);
                }
            }
        } else {
            for (String value : orderBy) {
                String[] split = value.split(":");
                sort = sort.and(Sort.by("asc".equals(split[1]) ? Sort.Direction.ASC : Sort.Direction.DESC, orderByColumnsMap.getOrDefault(split[0], split[0])));
            }
        }
        return sort;
    }
}
