package microYoga.dao.Imp;

import microYoga.dao.PhotoDao;
import microYoga.model.Photo;
import microYoga.model.PhotoWall;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PhotoDaoImp extends BaseDao implements PhotoDao {
    @Override
    public List<PhotoWall> getAllPhotoWalls(int pageNumber, int pageSize) throws SQLException {
        List<PhotoWall> items = new ArrayList<PhotoWall>();

        String limitSql = "";
        if(pageNumber != 0 && pageSize != 0)
            limitSql = String.format(" limit %s,%s", (pageNumber-1)*pageSize, pageSize);

        String selectSql = String.format("select Id, Name, Date from PhotoWall order by Date desc %s", limitSql);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while(rs.next()){
                        PhotoWall item = new PhotoWall();
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                        item.setPhotoList(getPhotoByPhotoWallBy(item.getId()));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<Photo> getPhotoByPhotoWallBy(String photoWallId) throws SQLException {
        List<Photo> items = new ArrayList<Photo>();

        String selectSql = String.format("select Id, PhotoWallId, Name, Url, ThumbUrl, Date from Photo where  PhotoWallId = '%s';", photoWallId);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    while(rs.next()){
                        Photo item = new Photo();
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setPhotoWallId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setUrl(rs.getString(i++));
                        item.setThumbUrl(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }


}
