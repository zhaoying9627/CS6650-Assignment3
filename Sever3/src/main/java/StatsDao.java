import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatsDao {
    private static BasicDataSource dataSource;

    public StatsDao() {
        dataSource = DBCPDataSource.getDataSource();
    }

    public Stats getStats(String userid) throws SQLException {
        Stats stats = null;
        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();
        String useridWithQuote = "'" + userid + "'";
        ResultSet resultSet = statement.executeQuery("SELECT * FROM stats WHERE userid = " + useridWithQuote);
        try {
            while (resultSet.next()) {
                int numLikes = resultSet.getInt(2);
                int numDislikes = resultSet.getInt(3);
                stats = new Stats(numLikes, numDislikes);
                break;
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
        return stats;
    }
}
