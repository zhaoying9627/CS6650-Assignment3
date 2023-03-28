import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchesDao {
    private static BasicDataSource dataSource;

    private final static int MAXIMUM_MATCHES = 100;

    public MatchesDao() {
        dataSource = DBCPDataSource.getDataSource();
    }

    public List<String> getMatches(String swiper) throws SQLException {
        List<String> matches = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
        String swiperWithQuote = "'" + swiper + "'";
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM matches WHERE swiper = " + swiperWithQuote);
            int matchesCount = 0;
            while (resultSet.next()) {
                String swipee = resultSet.getString(3);
                matches.add(swipee);
                matchesCount++;
                if (matchesCount >= MAXIMUM_MATCHES) {
                    break;
                }
            }
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return matches;
    }
}
