package employees;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetaDataDao {

    private MariaDbDataSource dataSource;

    public MetaDataDao(MariaDbDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<String> getTableNames() {
        try (Connection conn = dataSource.getConnection()){
            DatabaseMetaData metaData = conn.getMetaData();
            return getTableNamesByMetaData(metaData);
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot read table names", se);
        }
    }

    private List<String> getTableNamesByMetaData(DatabaseMetaData metaData) throws SQLException {
        try (ResultSet rs = metaData.getTables(null, null, null, null)){
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString(3);
                names.add(name);
            }
            return names;
        }
    }
}
