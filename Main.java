import java.sql.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://Cloud2020-88727:3306/baza";

    static final String USERNAME = "mkozak";
    static final String PASSWORD = "password";

    static Connection connection = null;
    static Statement statement = null;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Laczenie z baza danych");

            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();

            String sql = "";

            DatabaseMetaData dbm = connection.getMetaData();

            ResultSet tables = dbm.getTables(null, null, "osoby", null);

            if (!tables.next()) {
                sql = "CREATE TABLE osoby (id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, imie VARCHAR(20) NOT NULL, nazwisko VARCHAR(20) NOT NULL, email VARCHAR(30))";
                statement.executeUpdate(sql);

                sql = "INSERT INTO osoby VALUES (1, 'Jan', 'Kowalski', 'jan@kowalski.pl')";
                statement.executeUpdate(sql);

                sql = "INSERT INTO osoby VALUES (2, 'Anna', 'Kowalska', 'anna@kowalska.pl')";
                statement.executeUpdate(sql);

                sql = "INSERT INTO osoby VALUES (3, 'Mateusz', 'Kozak', 'mateusz@kozak.pl')";
                statement.executeUpdate(sql);

                System.out.println("Utworzono tabele i dodano rekordy");
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));

            boolean running = true;
            while (running) {
                System.out.println("\nWprowadz komende");
                String line = buffer.readLine();
                if (line.equals("exit")) running = false;
                else execCommand(line);
            }

            statement.close();
            connection.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    static void execCommand(String line) {
        try {
            String cmdString = line.substring(0, 6);
            System.out.println(cmdString);

            if (cmdString.equals("SELECT") || cmdString.equals("select")) displayTable(line);
            else statement.executeUpdate(line);
        } catch (SQLException se) {
            System.out.println("\nCos poszlo nie tak");
            se.printStackTrace();
        }
    }

    static void displayTable(String line) throws SQLException {
        ResultSet resultSet = statement.executeQuery(line);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        int columnsCount = resultSetMetaData.getColumnCount();

        for (int i = 1; i <= columnsCount; i++) {
            System.out.print(resultSetMetaData.getColumnName(i) + " ");
        }

        while (resultSet.next()) {
            System.out.print("\n");
            for (int i = 1; i <= columnsCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
            }
        }

        resultSet.close();
    }

}
