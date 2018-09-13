package microYoga.dao;

import microYoga.model.Member;

import java.sql.SQLException;
import java.util.List;

public interface MemberDao {

    void addMember(Member member) throws SQLException;

    void updateMember(Member member) throws SQLException;

    boolean isMemberExistedById(String id) throws SQLException;

    boolean isMobileExisted(String tel) throws SQLException;

    boolean isMobileDuplicated(String id, String tel) throws SQLException;

    List<Member> getMembers() throws SQLException;

    void deleteMember(String id) throws SQLException;

    Member authenticateUser(String tel, String password) throws SQLException;

    Member getMemberById(String id) throws SQLException;

    void changeMemberPwd(String id, String pwd);
}
