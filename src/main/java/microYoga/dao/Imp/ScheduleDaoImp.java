package microYoga.dao.Imp;

import microYoga.dao.ScheduleDao;
import microYoga.model.Schedule;
import microYoga.model.ScheduleExt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleDaoImp extends BaseDao implements ScheduleDao {

    @Override
    public List<Schedule> getSchedules() throws SQLException {
        List<Schedule> items = new ArrayList<Schedule>();
        String selectSql = String.format("select s.Id, s.TeacherId,s.CourseId,s.StartTime,s.EndTime,s.Capacity,t.Name,c.Name from Schedule s \n" +
                "left join Teacher t on s.TeacherId=t.id \n" +
                "left join Course c on s.CourseId=c.id \n" +
                "order by s.StartTime desc");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Schedule item = new Schedule();
                        item.setId(rs.getString(i++));
                        item.setTeacherId(rs.getString(i++));
                        item.setCourseId(rs.getString(i++));
                        item.setStartDateTime(rs.getString(i++));
                        item.setEndDateTime(rs.getString(i++));
                        item.setCapacity(rs.getInt(i++));
                        item.setTeacherName(rs.getString(i++));
                        item.setCourseName(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<Schedule> getSchedulesByFilter(String courseId, String teacherId, String startDate, String endDate) throws SQLException, ParseException {
        List<Schedule> items = new ArrayList<Schedule>();
        String selectSql = String.format("select s.Id, s.TeacherId,s.CourseId,s.StartTime,s.EndTime,s.Capacity,t.Name,c.Name,c.Rating,c.Avatar from Schedule s \n" +
                "left join Teacher t on s.TeacherId=t.id \n" +
                "left join Course c on s.CourseId=c.id ");

        if(StringUtils.isNotEmpty(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(endDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            endDate = sdf.format(cal.getTime());
        }


        if (StringUtils.isNotEmpty(courseId))
            selectSql += String.format(" and s.CourseId = '%s'", courseId);
        if (StringUtils.isNotEmpty(teacherId))
            selectSql += String.format(" and s.TeacherId = '%s'", teacherId);
        if (StringUtils.isNotEmpty(startDate))
            selectSql += String.format(" and s.StartTime >= '%s'", startDate);
        if (StringUtils.isNotEmpty(endDate))
            selectSql += String.format(" and s.EndTime <= '%s'", endDate);

        selectSql += " order by s.StartTime desc";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Schedule item = new Schedule();
                        item.setId(rs.getString(i++));
                        item.setTeacherId(rs.getString(i++));
                        item.setCourseId(rs.getString(i++));
                        item.setStartDateTime(rs.getString(i++));
                        item.setEndDateTime(rs.getString(i++));
                        item.setCapacity(rs.getInt(i++));
                        item.setTeacherName(rs.getString(i++));
                        item.setCourseName(rs.getString(i++));
                        item.setCourseRating(rs.getInt(i++));
                        item.setCourseAvatar(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void insertSchedule(Schedule schedule) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Schedule values(?,?,?,?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, schedule.getId());
                ps.setString(i++, schedule.getTeacherId());
                ps.setString(i++, schedule.getCourseId());
                ps.setString(i++, schedule.getStartDateTime());
                ps.setString(i++, schedule.getEndDateTime());
                ps.setInt(i++, schedule.getCapacity());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void updateSchedule(Schedule schedule) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update Schedule set TeacherId=?, CourseId=?, StartTime=?, EndTime=?, Capacity=? where id=?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, schedule.getTeacherId());
                ps.setString(i++, schedule.getCourseId());
                ps.setString(i++, schedule.getStartDateTime());
                ps.setString(i++, schedule.getEndDateTime());
                ps.setInt(i++, schedule.getCapacity());
                ps.setString(i++, schedule.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deleteSchedule(String id) throws SQLException {
        String deleteSql = String.format("update Schedule set IsDel=1 where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public void physicalDeleteSchedule(String id) throws SQLException {
        String deleteSql = String.format("delete from Schedule where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public Schedule getScheduleById(String scheduleId) throws SQLException {
        String selectSql = String.format("SELECT s.Id, s.TeacherId, s.CourseId, s.StartTime, s.EndTime, s.Capacity, t.Name,c.Name FROM Schedule s left join Teacher t on s.TeacherId=t.id left join Course c on s.CourseId=c.id where s.Id = '%s';", scheduleId);
        Schedule item = new Schedule();
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setTeacherId(rs.getString(i++));
                        item.setCourseId(rs.getString(i++));
                        item.setStartDateTime(rs.getString(i++));
                        item.setEndDateTime(rs.getString(i++));
                        item.setCapacity(rs.getInt(i++));
                        item.setTeacherName(rs.getString(i++));
                        item.setCourseName(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public List<ScheduleExt> getOneDayScheduledCourses(String dateStr, String memberId) throws SQLException {
        List<ScheduleExt> items = new ArrayList<ScheduleExt>();
        String selectSql = "select s.Id, s.TeacherId, s.CourseId, s.StartTime, s.EndTime, s.Capacity, c.Name, c.Avatar, c.Rating, t.Name from Schedule s ";
        selectSql += " join Course c on s.CourseId = c.Id ";
        selectSql += " join Teacher t on s.TeacherId = t.Id ";
        selectSql += String.format(" where substr(s.StartTime, 1, 10) = '%s'", dateStr);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        ScheduleExt item = new ScheduleExt();
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setTeacherId(rs.getString(i++));
                        item.setCourseId(rs.getString(i++));
                        item.setStartDateTime(rs.getString(i++));
                        item.setEndDateTime(rs.getString(i++));
                        item.setCapacity(Integer.parseInt(rs.getString(i++)));
                        item.setCourseName(rs.getString(i++));
                        item.setCourseAvatar(rs.getString(i++));
                        item.setCourseRating(Integer.parseInt(rs.getString(i++)));
                        item.setTeacherName(rs.getString(i++));
                        item.setOrderedCount(getOrderedMemberCountByScheduleId(item.getId()));
                        String orderId = getOrderIdByScheduleIdAndMemberId(item.getId(), memberId);
                        item.setOrderId(orderId);
                        if(StringUtils.isEmpty(orderId))
                            item.setOrdered(false);
                        else
                            item.setOrdered(true);

                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    private int getOrderedMemberCountByScheduleId(String scheduleId) throws SQLException {
        String selectSql = String.format("select count(0) from Orders where ScheduleId = '%s'", scheduleId);
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

    private String getOrderIdByScheduleIdAndMemberId(String scheduleId, String memberId) throws SQLException {
        String selectSql = String.format("select Id from Orders where ScheduleId = '%s' and MemberId = '%s'", scheduleId, memberId);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
        }

        return "";
    }


}