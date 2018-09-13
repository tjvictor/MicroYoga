package microYoga.dao.Imp;

import microYoga.dao.MemberDao;
import microYoga.model.Member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MemberDaoImp extends BaseDao implements MemberDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void addMember(Member member) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into Member values(?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, member.getId());
                ps.setString(i++, member.getName());
                ps.setString(i++, member.getSex());
                ps.setString(i++, member.getTel());
                ps.setString(i++, member.getPassword());
                ps.setString(i++, member.getWeChat());
                ps.setString(i++, member.getJoinDate());
                ps.setString(i++, member.getExpireDate());
                ps.setInt(i++, member.getFee());
                ps.setString(i++, member.getRemark());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void updateMember(Member member) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "update Member set Name=?, Sex=?, Tel=?, Password=?, JoinDate=?, ExpireDate=?, Fee=?, Remark=? where id = ?";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                int i = 1;
                ps.setString(i++, member.getName());
                ps.setString(i++, member.getSex());
                ps.setString(i++, member.getTel());
                ps.setString(i++, member.getPassword());
                ps.setString(i++, member.getJoinDate());
                ps.setString(i++, member.getExpireDate());
                ps.setInt(i++, member.getFee());
                ps.setString(i++, member.getRemark());
                ps.setString(i++, member.getId());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public boolean isMemberExistedById(String id) throws SQLException {
        String selectSql = String.format("select count(0) from Member where id = '%s'", id);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next())
                        if (rs.getInt(1) > 0)
                            return true;
                    return false;
                }
            }
        }
    }

    @Override
    public boolean isMobileExisted(String tel) throws SQLException {
        String selectSql = String.format("select count(0) from Member where tel = '%s'", tel);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next())
                        if (rs.getInt(1) > 0)
                            return true;
                    return false;
                }
            }
        }
    }

    @Override
    public boolean isMobileDuplicated(String id, String tel) throws SQLException {
        String selectSql = String.format("select count(0) from Member where tel = '%s' and id != '%s'", tel, id);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next())
                        if (rs.getInt(1) > 0)
                            return true;
                    return false;
                }
            }
        }
    }

    @Override
    public List<Member> getMembers() throws SQLException {
        List<Member> items = new ArrayList<Member>();
        String selectSql = String.format("select * from Member;");

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while (rs.next()) {
                        int i = 1;
                        Member item = new Member();
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setSex(rs.getString(i++));
                        item.setTel(rs.getString(i++));
                        item.setPassword(rs.getString(i++));
                        item.setWeChat(rs.getString(i++));
                        item.setJoinDate(rs.getString(i++));
                        item.setExpireDate(rs.getString(i++));
                        item.setFee(rs.getInt(i++));
                        item.setRemark(rs.getString(i++));
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void deleteMember(String id) throws SQLException {
        String deleteSql = String.format("delete from Member where id = '%s'", id);
        delete(deleteSql);
    }

    @Override
    public Member authenticateUser(String tel, String password) throws SQLException {
        String selectSql = String.format("select Id, Name, Sex, Tel, JoinDate, ExpireDate, Fee, Remark from Member where Tel = '%s' and Password = '%s';", tel, password);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()){
                        int i = 1;
                        Member member = new Member();
                        member.setId(rs.getString(i++));
                        member.setName(rs.getString(i++));
                        member.setSex(rs.getString(i++));
                        member.setTel(rs.getString(i++));
                        member.setJoinDate(rs.getString(i++));
                        member.setExpireDate(rs.getString(i++));
                        member.setFee(rs.getInt(i++));
                        member.setRemark(rs.getString(i++));
                        return member;
                    }

                    return null;
                }
            }
        }
    }

    @Override
    public Member getMemberById(String id) throws SQLException {
        String selectSql = String.format("select * from Member where id = '%s';", id);
        Member item = new Member();
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setSex(rs.getString(i++));
                        item.setTel(rs.getString(i++));
                        item.setPassword(rs.getString(i++));
                        item.setJoinDate(rs.getString(i++));
                        item.setExpireDate(rs.getString(i++));
                        item.setFee(rs.getInt(i++));
                        item.setRemark(rs.getString(i++));
                    }
                }
            }
        }
        return item;
    }

    @Override
    public void changeMemberPwd(String id, String pwd) {
        String selectSql = String.format("update Member set Password = '%s' where id = '%s';", pwd, id);
    }


}
