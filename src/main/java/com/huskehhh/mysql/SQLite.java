package com.huskehhh.mysql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <a href="https://bukkit.org/threads/using-mysql-in-your-plugins.132309/">
 * SQL Driver provided by Husky on the Bukkit Forums
 * </a><br>
 * 
 * Connects to and uses a SQLite database
 * 
 * Modified to save in plugins/ directory, as the default sqlite-db/ directory had issues.
 *
 * @author tips48
 */
public class SQLite extends Database {
    private final String dbLocation;

    /**
     * Creates a new SQLite instance
     * 
     * @param dbLocation Location of the Database (Must end in .db)
     */
    public SQLite(String dbLocation) {
        this.dbLocation = dbLocation;
    }

    @Override
    public Connection openConnection() throws SQLException,
            ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }

        File file = new File(dbLocation);
        if (!(file.exists())) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Unable to create database!");
            }
        }
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
        return connection;
    }
}