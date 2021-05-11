package employees;

import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeesDao {

    private MariaDbDataSource ds;

    public EmployeesDao(MariaDbDataSource ds) {
        this.ds = ds;
    }

    public long createEmployee(String name) {
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement("insert into employees(emp_name) values (?)",
                     Statement.RETURN_GENERATED_KEYS)
             ){
            stmt.setString(1, name);
            stmt.executeUpdate();
            return getIdByStatement(stmt);

        } catch (SQLException se) {
            throw new IllegalStateException("Cannot insert");
        }
    }

    private long getIdByStatement(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()){
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IllegalStateException("Cannot get id");
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot get id, ", se);
        }
    }

    public void createEmployees(List<String> names) {
        try (Connection conn = ds.getConnection()){
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement("insert into employees(emp_name) values (?)")){
                for (String name: names) {
                    if (name.startsWith("x")) {
                        throw new IllegalArgumentException("Invalid name");
                    }
                    stmt.setString(1, name);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (IllegalArgumentException iae) {
                conn.rollback();
            }
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot insert ", se);
        }
    }


    public List<String> listEmployeeNames() {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select emp_name from employees")
        ){
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("emp_name");
                names.add(name);
            }
            return names;
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot select employees");
        }
    }

    public List<String> listOddEmployeeNames() {
        try (Connection conn = ds.getConnection();
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("select emp_name from employees order by emp_name")){
            if (!rs.next()) {
                return Collections.emptyList();
            }
            List<String> names = new ArrayList<>();
            names.add(rs.getString("emp_name"));
            while (rs.relative(2)) {
                names.add(rs.getString("emp_name"));
            }
            return names;
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot list names", se);
        }
    }

    public  void updateNames() {
        try (Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("select id, emp_name from employees")){
            while (rs.next()) {
                String name = rs.getString("emp_name");
                if (!name.startsWith("Jane")) {
                    rs.updateString("emp_name", "Mr. " + name);
                    rs.updateRow();
                }
            }
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot update names");
        }
    }

    public String findEmployeeNameById(long id) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("select emp_name from employees where id = ?")){
            ps.setLong(1, id);
            return selectNameByPreparedStatement(ps);
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot query: ", se);
        }
    }

    private String selectNameByPreparedStatement(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()){
            if (rs.next()) {
                String name = rs.getString("emp_name");
                return name;
            }
            throw new IllegalArgumentException("Not found");
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot query: ", se);
        }
    }
}
