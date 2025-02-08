package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        DbHandler dbhandler = new DbHandler();
        dbhandler.connectionToDb();
        dbhandler.createBasicStatement();
        dbhandler.createTable();
        dbhandler.addRecord(DbHandler.INSERTSTATEMENT1);
        dbhandler.addRecord(DbHandler.INSERTSTATEMENT2);
        dbhandler.addRecord(DbHandler.INSERTSTATEMENT3);
        dbhandler.selectQuery(DbHandler.SELECT1, "name");
        dbhandler.selectQuery("SELECT * FROM `AB` WHERE id>1;", "surname");
        dbhandler.updateQuery(DbHandler.UPDATE1);
        dbhandler.createPreparedStatement();
        dbhandler.deletedRecord();
        dbhandler.deleteTable();
    }
}

class DbHandler {
    static final String DB_url = "jdbc:mysql://157.158.57.40:3306/laboratory_db";
    Connection db = null;
    static final String USER = "student";
    static final String PASSWORD = "student";
    Statement stmt = null;

    static final String SQLSTATEMENT = "CREATE TABLE IF NOT EXISTS AB (ID int PRIMARY KEY AUTO_INCREMENT, name varchar(30), surname varchar(30),  country varchar(30), age int ,email varchar(30))";
    static final String INSERTSTATEMENT1 = "INSERT IGNORE INTO AB (name, surname, country, age,email) VALUES ('And','Bee','Polska',22,'dd@gmail.com')";
    static final String INSERTSTATEMENT2 = "INSERT IGNORE INTO AB (ID, name, surname, country, age,email) VALUES (5,'John','Woo','Peru',27,'ase@gmail.com')";
    static final String INSERTSTATEMENT3 = "INSERT IGNORE INTO AB (ID, name, surname, country, age,email) VALUES (71,'Tom','Zee','Urugway',122,'ee@gmail.com')";
    static final String SELECT1 = "SELECT * FROM `AB` WHERE id=71;";
    static final String UPDATE1 = "UPDATE AB SET name='Zibi' WHERE name LIKE 'And'";

    void connectionToDb() {
        try {
            db = DriverManager.getConnection(DB_url,
                    USER, PASSWORD);
            System.out.println("Polaczono do db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Etap 1: Blad polaczenia z baza");
        }
    }

    void createBasicStatement() {
        try {
            stmt = db.createStatement();
            System.out.println("Etap 2: Utworzono statement");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void createPreparedStatement() {
        try {
            PreparedStatement pstmt =
                    db.prepareStatement(
                            "UPDATE AB SET age = ? WHERE name LIKE ?;");
            pstmt.setInt(1, 99);
//            pstmt.setString(2 ,"name"); // to nie zadziala, bo ? reprezentuje tylko wartosci
            pstmt.setString(2, "Tom");
            pstmt.executeUpdate();
            System.out.println("Uzyto Prepared Statement");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void createTable() {
        int insertedRows = 0;
        try {
            insertedRows = stmt.executeUpdate(SQLSTATEMENT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Etap 3: Utworzono " + insertedRows + " rekordow");
    }

    void addRecord(String statement) {
        try {
            System.out.println("Etap 4: Utworzono statement");
            int insertedRows = stmt.executeUpdate(statement);
            System.out.println("Etap 5: Utworzono " + insertedRows + " rekordow");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void selectQuery(String select, String column) {
        try {
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
                System.out.println("Etap 7: " + rs.getString(column));
            }
        } catch (SQLException e) {
            System.out.println("Ten blad");
            throw new RuntimeException(e);
        }
    }

    void updateQuery(String select) {
        try {
            int updatedRows = stmt.executeUpdate(select);
            System.out.println("Etap 9: Zmieniono rekord");
        } catch (SQLException e) {
            System.out.println("Ten blad2");
            throw new RuntimeException(e);
        }
    }

    void deletedRecord() {
        try {
            int updatedRows = stmt.executeUpdate("DELETE FROM AB WHERE ID >71");
            System.out.println("Usunieto rekordy o ID > 71");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    void deleteTable(){
        try {
            Boolean delete = stmt.execute("DROP TABLE AB");
            System.out.println("Usunieto tabele");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}