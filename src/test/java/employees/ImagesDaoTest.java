package employees;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ImagesDaoTest {

    static EmployeesDao employeesDao;
    static MariaDbDataSource ds;
    static ImagesDao imagesDao;

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
        imagesDao = new ImagesDao(ds);
        Flyway flyway = Flyway
                .configure()
                .locations("filesystem:src/test/resources/employees/db")
                .dataSource(ds)
                .load();
        flyway.clean();
        flyway.migrate();
    }


    @Test
    void testImage() throws IOException{
        // a main/resourcesből tölti csak be
        imagesDao.saveImage("employees/training360.gif", ImagesDaoTest.class.getResourceAsStream("training360.gif"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = imagesDao.getImageByName("employees/training360.gif")){
            is.transferTo(baos);
        }
        assertTrue(baos.size() > 3000);
    }
}
// 08 09:44