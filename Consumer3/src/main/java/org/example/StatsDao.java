package org.example;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatsDao {
    private static BasicDataSource dataSource;

    public StatsDao() {
        dataSource = DBCPDataSource.getDataSource();
    }

    public void updateStats (String swiper, String leftOrRight) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String columnName = leftOrRight.equals("right") ? "numLikes" : "numDislikes";
        String sql =
                "INSERT INTO stats (userid, numLikes, numDislikes) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE "
                        + columnName + " = " + columnName + " + 1";
        try {
            conn = dataSource.getConnection();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, swiper);
            preparedStatement.setInt(2, leftOrRight.equals("right") ? 1 : 0);
            preparedStatement.setInt(3, leftOrRight.equals("right") ? 0 : 1);
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
