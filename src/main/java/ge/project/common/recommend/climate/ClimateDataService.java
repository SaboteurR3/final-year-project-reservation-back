package ge.project.common.recommend.climate;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
public class ClimateDataService {

    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_CLIMATE_DATA = "SELECT * FROM climate_data";

    public List<ClimateDataRow> getAllClimateData() {
        return jdbcTemplate.query(SELECT_ALL_CLIMATE_DATA, new ClimateDataRowMapper());
    }

    private static class ClimateDataRowMapper implements RowMapper<ClimateDataRow> {
        @Override
        public ClimateDataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            String country = rs.getString("country");
            double avgTemperature = rs.getDouble("avg_temperature");
            double avgRainfall = rs.getDouble("avg_rainfall");
            double avgHumidity = rs.getDouble("avg_humidity");
            return new ClimateDataRow(country, avgTemperature, avgRainfall, avgHumidity);
        }
    }
}

@Getter
class ClimateDataRow {
    private String country;
    private double avgTemperature;
    private double avgRainfall;
    private double avgHumidity;

    public ClimateDataRow(String country, double avgTemperature, double avgRainfall, double avgHumidity) {
        this.country = country;
        this.avgTemperature = avgTemperature;
        this.avgRainfall = avgRainfall;
        this.avgHumidity = avgHumidity;
    }
}
