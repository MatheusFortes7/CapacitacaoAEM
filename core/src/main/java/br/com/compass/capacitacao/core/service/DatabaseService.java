package br.com.compass.capacitacao.core.service;

import java.sql.Connection;

public interface DatabaseService {
    Connection getConnection();
}
