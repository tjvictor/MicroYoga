package microYoga.dao.Imp;


import microYoga.dao.NotificationDao;
import microYoga.model.Notification;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationDaoImp extends BaseDao implements NotificationDao {
    @Override
    public List<Notification> getNotifications() throws SQLException {
        List<Notification> items = new ArrayList<Notification>();
        String selectSql = "select * from Notification;";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Notification item = new Notification();
                        item.setId(rs.getString(i++));
                        item.setTitle(rs.getString(i++));
                        item.setContent(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public Notification getNotificationById(String id) throws SQLException {
        Notification item = new Notification();
        String selectSql = String.format("select * from Notification where id = '%s';", id);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setTitle(rs.getString(i++));
                        item.setContent(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public List<Notification> getTopNotificationBriefs(int topCount) throws SQLException {
        List<Notification> items = new ArrayList<Notification>();
        String selectSql = String.format("select * from Notification order by Date desc limit %s;", topCount);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Notification item = new Notification();
                        item.setId(rs.getString(i++));
                        item.setTitle(rs.getString(i++));
                        item.setContent("");
                        i++;
                        item.setDate(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void insertNotification(Notification item) throws SQLException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Notification values(?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, item.getId());
                ps.setString(i++, item.getTitle());
                ps.setString(i++, item.getContent());
                ps.setString(i++, item.getDate());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void updateNotification(Notification item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update Notification set Title=?, Content=? where id = ?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, item.getTitle());
                ps.setString(i++, item.getContent());
                ps.setString(i++, item.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deleteNotification(String id) throws SQLException {
        String deleteSql = String.format("delete from Notification where id = '%s';", id);
        update(deleteSql);
    }
}
