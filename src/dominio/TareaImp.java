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
import java.util.ArrayList;
import java.util.List;
import pojo.Tarea;

public class TareaImp {

    public static Respuesta obtenerTareasPorEstado(int idUsuario, int idEstado) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String consulta = "SELECT t.id_tarea, t.titulo, t.descripcion, t.fecha_realizar, t.fecha_creacion, e.nombre_estado " +
                                  "FROM tareas t JOIN estados_tarea e ON t.id_estado = e.id_estado " +
                                  "WHERE t.id_usuario = ? AND t.id_estado = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(consulta);
                prepararSentencia.setInt(1, idUsuario);
                prepararSentencia.setInt(2, idEstado);
                
                ResultSet resultado = prepararSentencia.executeQuery();
                List<Tarea> tareas = new ArrayList<>();
                while (resultado.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setIdTarea(resultado.getInt("id_tarea"));
                    tarea.setTitulo(resultado.getString("titulo"));
                    tarea.setDescripcion(resultado.getString("descripcion"));
                    tarea.setFechaRealizar(resultado.getDate("fecha_realizar"));
                    tarea.setFechaCreacion(resultado.getTimestamp("fecha_creacion"));
                    tarea.setNombreEstado(resultado.getString("nombre_estado"));
                    tareas.add(tarea);
                }
                respuesta.setError(false);
                respuesta.setDatos(tareas);
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

    public static Respuesta agregarTarea(Tarea tarea) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String sentencia = "INSERT INTO tareas (id_usuario, titulo, descripcion, fecha_realizar) VALUES (?, ?, ?, ?)";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, tarea.getIdUsuario());
                prepararSentencia.setString(2, tarea.getTitulo());
                prepararSentencia.setString(3, tarea.getDescripcion());
                prepararSentencia.setDate(4, tarea.getFechaRealizar());
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Tarea agregada correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo agregar la tarea.");
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

    public static Respuesta editarTarea(Tarea tarea) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String sentencia = "UPDATE tareas SET titulo = ?, descripcion = ?, fecha_realizar = ? WHERE id_tarea = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setString(1, tarea.getTitulo());
                prepararSentencia.setString(2, tarea.getDescripcion());
                prepararSentencia.setDate(3, tarea.getFechaRealizar());
                prepararSentencia.setInt(4, tarea.getIdTarea());
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Tarea editada correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo editar la tarea.");
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

    public static Respuesta eliminarTarea(int idTarea) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String sentencia = "DELETE FROM tareas WHERE id_tarea = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idTarea);
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Tarea eliminada correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo eliminar la tarea.");
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

    public static Respuesta cambiarEstadoTarea(int idTarea, int idEstadoNuevo) {
        Respuesta respuesta = new Respuesta();
        Connection conexionBD = ConexionBD.obtenerConexion();
        
        if (conexionBD != null) {
            try {
                String sentencia = "UPDATE tareas SET id_estado = ? WHERE id_tarea = ?";
                PreparedStatement prepararSentencia = conexionBD.prepareStatement(sentencia);
                prepararSentencia.setInt(1, idEstadoNuevo);
                prepararSentencia.setInt(2, idTarea);
                
                int filasAfectadas = prepararSentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Estado de la tarea actualizado.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo actualizar el estado de la tarea.");
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