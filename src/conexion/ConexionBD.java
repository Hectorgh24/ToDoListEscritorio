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
    
    public static Connection obtenerConexion() {
        Connection conexionBD = null;
        try {
            String url = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false",
                    Constantes.HOSTNAME, Constantes.PUERTO, Constantes.NOMBRE_BD);
            conexionBD = DriverManager.getConnection(url, Constantes.USUARIO_BD, Constantes.PASSWORD_BD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conexionBD;
    }
}
