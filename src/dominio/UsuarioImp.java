/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import conexion.ConexionBD;
import dto.Respuesta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pojo.Usuario;
import utilidad.Constantes;
import utilidad.Utilidades;

public class UsuarioImp {

    public static Respuesta iniciarSesion(String rfc, String password) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String consulta = "SELECT id_usuario, rfc, nombre, apellido_paterno, apellido_materno, fecha_nacimiento " +
                                  "FROM usuarios WHERE rfc = ? AND password = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setString(1, rfc);
                prepararSentencia.setString(2, Utilidades.cifrarPassword(password));
                
                ResultSet resultado = prepararSentencia.executeQuery();
                if (resultado.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(resultado.getInt("id_usuario"));
                    usuario.setRfc(resultado.getString("rfc"));
                    usuario.setNombre(resultado.getString("nombre"));
                    usuario.setApellidoPaterno(resultado.getString("apellido_paterno"));
                    usuario.setApellidoMaterno(resultado.getString("apellido_materno"));
                    usuario.setFechaNacimiento(resultado.getDate("fecha_nacimiento"));
                    
                    respuesta.setError(false);
                    respuesta.setMensaje("Bienvenido " + usuario.getNombre());
                    respuesta.setDatos(usuario);
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("RFC o contraseña incorrectos.");
                }
                conexionBD.close();
            } catch (SQLException e) {
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }
        return respuesta;
    }

    public static Respuesta registrarUsuario(Usuario usuario) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String sentencia = "INSERT INTO usuarios (rfc, password, nombre, apellido_paterno, apellido_materno, fecha_nacimiento) " +
                                   "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, usuario.getRfc());
                prepararSentencia.setString(2, Utilidades.cifrarPassword(usuario.getPassword()));
                prepararSentencia.setString(3, usuario.getNombre());
                prepararSentencia.setString(4, usuario.getApellidoPaterno());
                prepararSentencia.setString(5, usuario.getApellidoMaterno());
                prepararSentencia.setDate(6, usuario.getFechaNacimiento());
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Usuario registrado correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo registrar el usuario.");
                }
                conexionBD.close();
            } catch (SQLException e) {
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }
        return respuesta;
    }

    public static Respuesta cambiarPassword(String rfc, String nuevaPassword) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String sentencia = "UPDATE usuarios SET password = ? WHERE rfc = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, Utilidades.cifrarPassword(nuevaPassword));
                prepararSentencia.setString(2, rfc);
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Contraseña actualizada correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo actualizar la contraseña.");
                }
                conexionBD.close();
            } catch (SQLException e) {
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }
        return respuesta;
    }
}