package microYoga.dao.Imp;

import microYoga.dao.NewsDao;
import microYoga.model.News;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewsDaoImp  extends BaseDao implements NewsDao {
    @Override
    public List<News> getAllNews() throws SQLException {
        return null;
    }

    @Override
    public List<News> getAllNewsBrief() throws SQLException {
        List<News> items = new ArrayList<News>();
        String selectSql = "select Id, Title, Img, Date from News;";

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        News item = new News();
                        item.setId(rs.getString(i++));
                        item.setTitle(rs.getString(i++));
                        item.setImg(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public News getNewsById(String id) throws SQLException {
        News item = new News();
        String selectSql = String.format("select Id, Title, Content, Img, Date from News where Id = '%s';", id);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setTitle(rs.getString(i++));
                        item.setContent(rs.getString(i++));
                        item.setImg(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public void insertNews(News item) throws SQLException {

    }

    @Override
    public void updateNews(News item) throws SQLException {

    }

    @Override
    public void deleteNews(String id) throws SQLException {

    }
}
