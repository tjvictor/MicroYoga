package microYoga.dao.Imp;

import microYoga.dao.CourseDao;
import microYoga.model.Course;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseDaoImp extends BaseDao implements CourseDao {

    @Override
    public List<Course> getCourses() throws SQLException {
        List<Course> items = new ArrayList<Course>();
        String selectSql = String.format("select * from Course");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Course item = new Course();
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setAvatar(rs.getString(i++));
                        item.setAvatarCategory(rs.getString(i++));
                        item.setIntroduction(rs.getString(i++));
                        item.setRating(rs.getInt(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public Course getCourseById(String id) throws SQLException {
        List<Course> items = new ArrayList<Course>();
        String selectSql = String.format("select * from Course where id = '%s'", id);
        Course item = new Course();
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setAvatar(rs.getString(i++));
                        item.setAvatarCategory(rs.getString(i++));
                        item.setIntroduction(rs.getString(i++));
                        item.setRating(rs.getInt(i++));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public void insertCourse(Course course) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "insert into Course values(?,?,?,?,?,?)";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, course.getId());
                ps.setString(i++, course.getName());
                ps.setString(i++, course.getAvatar());
                ps.setString(i++, course.getAvatarCategory());
                ps.setString(i++, course.getIntroduction());
                ps.setInt(i++, course.getRating());
                ps.executeUpdate();
            }
        }
        course.setIntroduction(course.getIntroduction());
    }

    @Override
    public void updateCourse(Course course) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "update Course set Name=?, Avatar=?, AvatarCategory=?, Introduction=?, Rating=? where id = ?";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, course.getName());
                ps.setString(i++, course.getAvatar());
                ps.setString(i++, course.getAvatarCategory());
                ps.setString(i++, course.getIntroduction());
                ps.setInt(i++, course.getRating());
                ps.setString(i++, course.getId());
                ps.executeUpdate();
            }
        }
        course.setIntroduction(course.getIntroduction());
    }

    @Override
    public void deleteCourse(String id) throws SQLException {
        String deleteSql = String.format("delete from Course where id = '%s'", id);
        delete(deleteSql);
    }
}
