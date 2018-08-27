package microYoga.dao.Imp;

import microYoga.dao.NewsDao;
import microYoga.model.News;

import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.util.List;

@Component
public class NewsDaoImp  extends BaseDao implements NewsDao {
    @Override
    public List<News> getAllNews() throws SQLException {
        return null;
    }

    @Override
    public News getNewsById(String id) throws SQLException {
        return null;
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
