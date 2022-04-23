
package com.emergentes.utiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConexionDB {
    static String driver = "com.mysql.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/db_agenda";
    static String usuario = "root";
    static String password = "";
    
Connection conn = null;

public ConexionDB(){
    try{
        // especificar de driver
        Class.forName(driver);
        //establecer la conexion a la base de dtaos
        conn = DriverManager.getConnection(url, usuario, password);
        // verificar si la conexion fue exitosa 
        if (conn != null){
            System.out.println("conexion OK  " + conn);
        }
    }catch (SQLException ex){
        System.out.println("Error de sql  " +ex.getMessage());
        
    } catch (ClassNotFoundException ex){
        Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null, ex);
    }
    
}

public Connection conectar(){
    return conn;
}

public void desconectar(){
    try {
        conn.close();
    }catch (SQLException ex){
        Logger.getLogger(ConexionDB.class.getName()).log(Level.SEVERE, null , ex);
    }
}
}

    
