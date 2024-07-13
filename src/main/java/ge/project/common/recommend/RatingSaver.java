package ge.project.common.recommend;

import ge.project.common.recommend.climate.ClimateData;
import ge.project.common.repository.Country;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RatingSaver {
    private static final String url = "jdbc:postgresql://localhost/bookings";
    private static final String user = "postgres";
    private static final String password = "56609";

    private static final String INSERT_USERS_SQL = "INSERT INTO Ratings" +
            "  (countryId, preferredTemp, preferredHumid, preferredRain, userId, rating) VALUES " +
            " (?, ?, ?, ?, ?, ?);";


    public static void saveRating(Country country, ClimateData climateData, int userId, double rating){
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password);

             // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, String.valueOf(Country.valueOf(country.getName()).ordinal()));
            preparedStatement.setString(2, String.valueOf(climateData.getAvgTemperature()));
            preparedStatement.setString(3, String.valueOf(climateData.getAvgHumidity()));
            preparedStatement.setString(4, String.valueOf(climateData.getAvgRainfall()));
            preparedStatement.setString(5, String.valueOf(userId));
            preparedStatement.setString(6, String.valueOf(rating));

            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            System.out.println(e.getMessage());
        }

    }
}
