package microYoga.dao.Imp;

import microYoga.dao.VideoDao;
import microYoga.model.Video;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoDaoImp extends BaseDao implements VideoDao {
    @Override
    public List<Video> getVideos() throws SQLException {
        List<Video> items = new ArrayList<Video>();

        String selectSql = String.format("select * from Video");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Video item = new Video();
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setBrief(rs.getString(i++));
                        item.setType(rs.getString(i++));
                        item.setImgPath(rs.getString(i++));
                        item.setVideoPath(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }
}
