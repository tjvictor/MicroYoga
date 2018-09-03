package microYoga.dao.Imp;

import microYoga.dao.WeChatDao;
import microYoga.model.wx.SNSUserInfo;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class WeChatDaoImp extends BaseDao implements WeChatDao {

    @Override
    public void insertSNSUserInfo(SNSUserInfo item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbActivityConnectString)){
            String insertSql = "insert into SNSUserInfo values(?,?,?,?,?,?,?,?)";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, item.getOpenId());
                ps.setString(i++, item.getNickName());
                ps.setString(i++, item.getSex());
                ps.setString(i++, item.getCountry());
                ps.setString(i++, item.getProvince());
                ps.setString(i++, item.getCity());
                ps.setString(i++, item.getHeadImgUrl());
                ps.setString(i++, item.getPrivilegeString());
                ps.executeUpdate();
            }
        }
    }
}
