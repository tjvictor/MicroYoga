package microYoga;

import microYoga.utils.CommonUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public class BatchOperation {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        Class.forName("org.sqlite.JDBC");

        batchInsertSchedule();
    }

    public static void batchInsertSchedule() throws SQLException, ParseException {
        String dbConnectString = "jdbc:sqlite:C:/Users/1464202/git/tjvictor/MicroYoga/src/main/resources/yoga.db";
        Date startDate = CommonUtils.getDateByString("2018-08-31");
        Date endDate = CommonUtils.getDateByString("2018-12-31");
        String startTime = "19:30";
        String endTime = "20:30";
        String capacity = "12";
        String courseId = "3c3bbf6c-7767-4428-9462-232954e6e716";
        String teacherId = "8b29bbb7-b797-4343-87d3-245c91636024";
        String weekName = "周六";
        int result = 0;
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Schedule values(?,?,?,?,?,?)";
            while(startDate.getTime()<=endDate.getTime()) {
                startDate = CommonUtils.dateAddDay(startDate, 1);
                if (CommonUtils.getWeekName(startDate).equals(weekName)) {
                    result ++;
                    try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                        int i = 1;
                        ps.setString(i++, UUID.randomUUID().toString());
                        ps.setString(i++, teacherId);
                        ps.setString(i++, courseId);
                        ps.setString(i++, String.format("%s %s", CommonUtils.getDateStr(startDate), startTime));
                        ps.setString(i++, String.format("%s %s", CommonUtils.getDateStr(startDate), endTime));
                        ps.setString(i++, capacity);
                        ps.executeUpdate();
                    }
                }
            }
        }
        System.out.println("Result: "+result);
    }
}
