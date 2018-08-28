package microYoga.dao.Imp;

import microYoga.dao.OrderDao;
import microYoga.dao.ScheduleDao;
import microYoga.model.Order;
import microYoga.model.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderDaoImp extends BaseDao implements OrderDao {

    @Autowired
    private ScheduleDao scheduleDaoImp;


    @Override
    public List<Order> getOrdersByFilter(String startDate, String endDate) throws SQLException, ParseException {

        List<Schedule> schedules = scheduleDaoImp.getSchedulesByFilter("", "", startDate, endDate);
        List<Order> subSchedules = new ArrayList<Order>();
        String selectSql = "select count(0) from Order where scheduleId = '%s'";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                for (Schedule scheduleItem : schedules)
                    try (ResultSet rs = stmt.executeQuery(String.format(selectSql, scheduleItem.getId()))) {
                        if (rs.next()) {
                            Order item = new Order();
                            item.setScheduleId(scheduleItem.getId());
                            item.setCourseName(scheduleItem.getCourseName());
                            item.setTeacherName(scheduleItem.getTeacherName());
                            item.setStartDateTime(scheduleItem.getStartDateTime());
                            item.setEndDateTime(scheduleItem.getEndDateTime());
                            item.setCapacity(scheduleItem.getCapacity());
                            item.setSubCount(rs.getInt(1));

                            subSchedules.add(item);
                        }
                    }
            }
        }

        return subSchedules;
    }

    @Override
    public List<Order> getOrderedMembers(String subScheduleId) throws SQLException {
        List<Order> subSchedules = new ArrayList<Order>();

        String selectSql = String.format("select sub.Id, sub.MemberId, m.Name, m.Tel from Order " +
                "join Member m on sub.MemberId = m.Id " +
                "where sub.ScheduleId = '%s'", subScheduleId);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Order item = new Order();
                        item.setId(rs.getString(i++));
                        item.setMemberId(rs.getString(i++));
                        item.setMemberName(rs.getString(i++));
                        item.setMemberTel(rs.getString(i++));

                        subSchedules.add(item);
                    }
                }
            }

            return subSchedules;
        }
    }

    @Override
    public List<Order> getMobileOrdersByFilter(String date, String userId) throws SQLException, ParseException {
        List<Schedule> schedules = scheduleDaoImp.getSchedulesByFilter("", "", date, date);
        List<Order> subSchedules = new ArrayList<Order>();
        String selectSql = "select count(0) from Order where scheduleId = '%s'";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                for (Schedule scheduleItem : schedules)
                    try (ResultSet rs = stmt.executeQuery(String.format(selectSql, scheduleItem.getId()))) {
                        if (rs.next()) {
                            Order item = new Order();
                            item.setScheduleId(scheduleItem.getId());
                            item.setCourseName(scheduleItem.getCourseName());
                            item.setTeacherName(scheduleItem.getTeacherName());
                            item.setStartDateTime(df.format(df.parse(scheduleItem.getStartDateTime())));
                            item.setEndDateTime(df.format(df.parse(scheduleItem.getEndDateTime())));
                            item.setCapacity(scheduleItem.getCapacity());
                            item.setCourseRating(scheduleItem.getCourseRating());
                            item.setCourseAvatar(scheduleItem.getCourseAvatar());
                            item.setSubCount(rs.getInt(1));

                            subSchedules.add(item);
                        }
                    }

                selectSql = "select id from Order where scheduleId = '%s' and memberId = '%s'";
                for (Order subScheduleItem : subSchedules)
                    try (ResultSet rs = stmt.executeQuery(String.format(selectSql, subScheduleItem.getScheduleId(), userId))) {
                        if (rs.next()) {
                            subScheduleItem.setId(rs.getString(1));
                            subScheduleItem.setMemberId(userId);
                        }
                    }
            }
        }

        return subSchedules;
    }

    @Override
    public void insertOrder(Order subSchedule) throws SQLException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String insertSql = String.format("insert into Order values('%s','%s','%s', '%s');",
                subSchedule.getId(), subSchedule.getScheduleId(), subSchedule.getMemberId(), df.format(new Date()));

        insert(insertSql);
    }

    @Override
    public void updateOrder(Order subSchedule) throws SQLException {

    }

    @Override
    public void deleteOrder(Order subSchedule) throws SQLException {
        String deleteSql = String.format("delete from Order where ScheduleId = '%s' and MemberId = '%s';",
                subSchedule.getScheduleId(), subSchedule.getMemberId());
        delete(deleteSql);
    }

    @Override
    public void deleteOrder(String id) throws SQLException {
        String deleteSql = String.format("delete from Order where id = '%s';", id);
        delete(deleteSql);
    }

    @Override
    public int getOrderMemberCountByScheduleId(String scheduleId) throws SQLException {
        String selectSql = String.format("select count(0) from Order where ScheduleId = '%s'", scheduleId);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        return 0;
    }

}
