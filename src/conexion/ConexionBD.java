/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utilidad.Constantes;

public class ConexionBD {
    
    private static final String DRIVER = "jdbc:mysql://";
    private static final String PARAMETROS_SSL = "?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String URL_CONEXION = DRIVER + Constantes.HOSTNAME + ":" + Constantes.PUERTO + "/" + Constantes.NOMBRE_BD + PARAMETROS_SSL;
    
    public static Connection obtenerConexion() {
        Connection conexionBD = null;
        try {
            conexionBD = DriverManager.getConnection(URL_CONEXION, Constantes.USUARIO_BD, Constantes.PASSWORD_BD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conexionBD;
    }
}