/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ittepic.ejbs.EJBOperacionesPrueba;
import mx.edu.ittepic.utils.Message;

/**
 *
 * @author kon_n
 */
@WebServlet(name = "CrearUsuario", urlPatterns = {"/CrearUsuario"})
public class CrearUsuario extends HttpServlet {
    @EJB
    private EJBOperacionesPrueba ejb;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter p = response.getWriter();
        
        Message m = new Message();
        m.setCode(401);
        m.setMessage("Don't enter");
        m.setDetail("Not autorized method");
        
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        p.print(gson.toJson(m));
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
            response.setHeader("Cache-control", "no-store");

            PrintWriter p = response.getWriter();

            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            if(nombre==null || nombre.equals("")){
                Message m = new Message();
                m.setCode(HttpServletResponse.SC_BAD_REQUEST);
                m.setMessage("No se encontró el usuario");
                m.setDetail("El o los parámetros enviados son incorrectos");
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                p.print(gson.toJson(m));
            }else {
                p.write(ejb.createUsuario(nombre, email, password));
            }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
