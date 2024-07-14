package ge.project.common.recommend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class RecommenderController {

    private RecommenderService recommenderService;

    @GetMapping("/{userId}")
    public List<CountryParam> recommendCountries(@PathVariable Long userId) {
        return recommenderService.recommendCountries(userId);
    }
}
