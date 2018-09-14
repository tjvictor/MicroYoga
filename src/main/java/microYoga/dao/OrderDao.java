package microYoga.dao;

import microYoga.model.Order;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public interface OrderDao {
    List<Order> getOrdersByFilter(String startDate, String endDate) throws SQLException, ParseException;

    List<Order> getOrderedMembers(String subScheduleId) throws SQLException;

    List<Order> getMobileOrdersByFilter(String date, String userId) throws SQLException, ParseException;

    List<Order> getOrdersByScheduleId(String scheduleId) throws SQLException;

    void insertOrder(Order order) throws SQLException;

    void updateOrder(Order order) throws SQLException;

    void deleteOrder(Order order) throws SQLException;

    void deleteOrder(String id) throws SQLException;

    int getOrderMemberCountByScheduleId(String scheduleId) throws SQLException;
}
