
package com.emergentes.controlador;

import com.emergentes.modelo.Libro;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            String op;
            op = (request.getParameter("op") !=null)? request.getParameter("op"): "list";
            
            ArrayList<Libro> lista = new ArrayList<Libro>();
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            
            if(op.equals("list")){
               // para listar los datos
               String sql = "select * from libros";
               // consulta de selleccion y almacenamiento en una coleccion
               ps = conn.prepareStatement(sql);
               rs = ps.executeQuery();
               
               while(rs.next()){
                   Libro lib = new Libro();
                   
                   lib.setId(rs.getInt("id"));
                   lib.setIsbn(rs.getString("isbn"));
                   lib.setTitulo(rs.getString("titulo"));
                   lib.setCategoria(rs.getString("categoria"));
                   lista.add(lib);
               }
               request.setAttribute("lista", lista);
               // enviar al index para mostrar la informacion
               request.getRequestDispatcher("index.jsp").forward(request, response);
            }
            if(op.equals("nuevo")){
                // insertar un objeto de la clase libro
                Libro li = new Libro();
                
                System.out.println(li.toString());
                
                //elobjeto se pone como atributo de request
                request.setAttribute("lib", li);
                // redireccionar a editar
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                
            }
            if(op.equals("eliminar")){
                // obtener el id 
                int id = Integer.parseInt(request.getParameter("id"));
                // REALIZR LA ELIMINACION EN LA BASE DEDATOS
                String sql = "delete from libros where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                // redireccionar a maincontroller
                response.sendRedirect("MainController");
            }
            
            if(op.equals("editar")){
                //obtener id
                int id = Integer.parseInt(request.getParameter("id"));
                try{
                    Libro li = new Libro();
                    
                    ps = conn.prepareStatement("select * from libros where id = ?");
                    ps.setInt(1, id);
                    rs = ps.executeQuery();
                    if(rs.next()){
                        li.setId(rs.getInt("id"));
                        li.setIsbn(rs.getString("isbn"));
                        li.setTitulo(rs.getString("titulo"));
                        li.setCategoria(rs.getString("categoria"));
                    }
                    request.setAttribute("lib",li );
                    request.getRequestDispatcher("editar.jsp").forward(request, response);
                    
                }catch (SQLException ex){
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null , ex);
            }    
            }
            }catch (SQLException ex){
                System.out.println("ERROR al ConeCtar" +ex.getMessage());
            }    
        }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    try{
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("valor de id   "+ id);
        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        String categoria = request.getParameter("categoria");
        
        Libro lib = new Libro();
        
        lib.setIsbn(isbn);
        lib.setId(id);
        lib.setTitulo(titulo);
        lib.setCategoria(categoria);
        
        ConexionDB canal = new ConexionDB();
        Connection conn = canal.conectar();
        
        PreparedStatement ps;
        ResultSet rs;

        
        if(id == 0){
            // nuevo registro
            String sql = "insert into libros (isbn, titulo, categoria) values (?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, lib.getIsbn());
            ps.setString(2, lib.getTitulo());
            ps.setString(3, lib.getCategoria());
            ps.executeUpdate();
            
        }else{
            //editar 
            String sql ="update libros set isbn=?, titulo=?, categoria=? where id=?" ;
        
        try{
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, lib.getIsbn());
            ps.setString(2, lib.getTitulo());
            ps.setString(3, lib.getCategoria());
            ps.setInt(4, lib.getId());
            
            ps.executeUpdate();
        
        }catch (SQLException ex){
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null , ex);
            }    
        }
        response.sendRedirect("MainController");
    } catch(SQLException ex){
            System.out.println("error en sql " +ex.getMessage());
    }
}
}
