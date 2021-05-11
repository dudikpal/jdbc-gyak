package employees;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;

import org.mariadb.jdbc.MariaDbDataSource;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.List;

public class MetaDataDaoTest {

    static EmployeesDao employeesDao;
    static MariaDbDataSource ds;
    static MetaDataDao metaDataDao;

    @BeforeAll
    static void init() throws SQLException {
        ds = new MariaDbDataSource();
        ds.setUrl("jdbc:mariadb://localhost:3308/employees?useUnicode=true");
        ds.setUser("employees");
        ds.setPassword("employees");
        employeesDao = new EmployeesDao(ds);
    }
    @BeforeEach
    public void setUp() throws SQLException{
        employeesDao = new EmployeesDao(ds);
        metaDataDao = new MetaDataDao(ds);
        Flyway flyway = Flyway

                .configure()
                .locations("filesystem:src/test/resources/employees/db")
                .dataSource(ds)
                .load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testTableNames() {
        List<String> names = metaDataDao.getTableNames();
        System.out.println(names);

        assertTrue(names.contains("employees"));
    }
}
