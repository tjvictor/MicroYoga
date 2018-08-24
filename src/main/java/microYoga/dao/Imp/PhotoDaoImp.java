package microYoga.dao.Imp;

import microYoga.dao.PhotoDao;
import microYoga.model.Photo;
import microYoga.model.PhotoWall;

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
public class PhotoDaoImp extends BaseDao implements PhotoDao {
    @Override
    public List<PhotoWall> getAllPhotoWallEntities() throws SQLException {
        List<PhotoWall> items = new ArrayList<PhotoWall>();
        String selectSql = String.format("select Id, Name, Date from PhotoWall order by Date");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        PhotoWall item = new PhotoWall();
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<PhotoWall> getAllPhotoWallWithPhotos(int pageNumber, int pageSize) throws SQLException {
        List<PhotoWall> items = new ArrayList<PhotoWall>();

        String limitSql = "";
        if (pageNumber != 0 && pageSize != 0)
            limitSql = String.format(" limit %s,%s", (pageNumber - 1) * pageSize, pageSize);

        String selectSql = String.format("select Id, Name, Date from PhotoWall order by Date desc %s", limitSql);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        PhotoWall item = new PhotoWall();
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setDate(rs.getString(i++));
                        item.setPhotoList(getPhotoByPhotoWallId(item.getId()));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<Photo> getPhotoByPhotoWallId(String photoWallId) throws SQLException {
        List<Photo> items = new ArrayList<Photo>();

        String selectSql = String.format("select Id, PhotoWallId, Name, Url, ThumbUrl, Date from Photo where  PhotoWallId = '%s';", photoWallId);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
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

    @Override
    public int getPhotoWallTotalCount() throws SQLException {
        String selectSql = String.format("select count(0) from PhotoWall");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    if(rs.next())
                        return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    @Override
    public void addPhoto(Photo item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Photo values(?,?,?,?,?,?);";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, item.getId());
                ps.setString(i++, item.getPhotoWallId());
                ps.setString(i++, item.getName());
                ps.setString(i++, item.getUrl());
                ps.setString(i++, item.getThumbUrl());
                ps.setString(i++, item.getDate());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deletePhoto(String id) throws SQLException {
        String deleteSql = String.format("delete from Photo where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public void addPhotoWall(PhotoWall item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into PhotoWall values(?,?,?);";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, item.getId());
                ps.setString(i++, item.getName());
                ps.setString(i++, item.getDate());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void updatePhotoWall(PhotoWall item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update PhotoWall set Name=? where Id=?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, item.getName());
                ps.setString(i++, item.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deletePhotoWall(String id) throws SQLException {
        String deleteSql = String.format("delete from PhotoWall where id = '%s'", id);
        delete(deleteSql);
    }


}
