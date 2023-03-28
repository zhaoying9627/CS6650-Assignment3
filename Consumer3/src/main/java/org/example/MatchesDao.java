package org.example;
import java.sql.*;
import org.apache.commons.dbcp2.*;

public class MatchesDao {
    private static BasicDataSource dataSource;

    public MatchesDao() {
        dataSource = DBCPDataSource.getDataSource();
    }

    public void createMatches(Match newMatch) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String insertQueryStatement = "INSERT INTO matches (swiper, swipee) " +
                "VALUES (?,?)";
        try {
            conn = dataSource.getConnection();
            preparedStatement = conn.prepareStatement(insertQueryStatement);
            preparedStatement.setString(1, newMatch.getSwiper());
            preparedStatement.setString(2, newMatch.getSwipee());
            // execute insert SQL statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
