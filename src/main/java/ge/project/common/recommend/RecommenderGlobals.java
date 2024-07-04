package ge.project.common.recommend;

import org.springframework.jdbc.core.JdbcTemplate;

public class RecommenderGlobals {
    private JdbcTemplate jdbcTemplate;

    public static int MAX_COUNTRIES;
    public static float MAX_TEMPERATURE;
    public static float MIN_TEMPERATURE;
    public static float MAX_HUMIDITY;
    public static float MIN_HUMIDITY;
    public static float MAX_RAINFALL;
    public static float MIN_RAINFALL;

    public void loadGlobals() {
        String sql = "SELECT " +
                "MAX(avg_temperature) AS max_temperature, " +
                "MIN(avg_temperature) AS min_temperature, " +
                "MAX(avg_rainfall) AS max_rainfall, " +
                "MIN(avg_rainfall) AS min_rainfall, " +
                "MAX(avg_humidity) AS max_humidity, " +
                "MIN(avg_humidity) AS min_humidity, " +
                "COUNT(1) AS max_countries " +
                "FROM climate_data";

        jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            this.MAX_TEMPERATURE = rs.getFloat("max_temperature");
            this.MIN_TEMPERATURE = rs.getFloat("min_temperature");
            this.MAX_RAINFALL = rs.getFloat("max_rainfall");
            this.MIN_RAINFALL = rs.getFloat("min_rainfall");
            this.MAX_HUMIDITY = rs.getFloat("max_humidity");
            this.MIN_HUMIDITY = rs.getFloat("min_humidity");
            this.MAX_COUNTRIES = rs.getInt("max_countries");
            return null;
        });
    }
}
