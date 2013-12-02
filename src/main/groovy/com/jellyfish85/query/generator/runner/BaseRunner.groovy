package com.jellyfish85.query.generator.runner

import com.jellyfish85.dbaccessor.manager.DatabaseManager

import java.sql.Connection

public class BaseRunner {

    public BaseRunner() {}

    private static DatabaseManager manager = new DatabaseManager()

    public Connection getConnection() {
        return this.manager.conn()
    }

    public void databaseInitialize() {
        println("database initialize..")
        println(".. getting connection ..")
        this.manager.connect()
    }

    public void databaseFinalize() {
        this.manager.jCommit()
        this.manager.jClose()
    }
}
