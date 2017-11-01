/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.ittepic.ejbs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import mx.edu.ittepic.entitites.Usuarios;
import mx.edu.ittepic.utils.Message;

/**
 *
 * @author kon_n
 */
@Stateless
public class EJBOperacionesPrueba {
    @PersistenceContext
    private EntityManager em;
    public String getUsuarioByEmail(String email,String password){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Message m = new Message();
        Query q;
        q = em.createNamedQuery("Usuarios.findByEmail").setParameter("email", email);
        Usuarios u= (Usuarios)q.getSingleResult();
        
        if(u!=null){
            System.out.print("pass "+u.getPassword());
            if(u.getPassword().equals(password)){
                m.setCode(HttpServletResponse.SC_OK);
                m.setMessage("Usuario Correcto");
                
            }else{
                m.setCode(HttpServletResponse.SC_BAD_REQUEST);
                m.setMessage("Usuario o contraseña Incorrecto");
                
            }
        }else{
                m.setCode(HttpServletResponse.SC_BAD_REQUEST);
                m.setMessage("El usuario no existe");
                m.setDetail("Usuario no encontrado");
        }
        return gson.toJson(m);
        
    }
    public String getUsuarios() {
        Query q;
        List<Usuarios> listUsuarios;        
        q = em.createNamedQuery("Usuarios.findAll");        
        listUsuarios = q.getResultList();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String result = gson.toJson(listUsuarios);
        return result;
    }
    public String createUsuario(String nombre,String email,String password) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Message m = new Message();
        
        Usuarios u = new Usuarios();
        
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword(password);
        u.setActivo(true);
        try {
            em.persist(u);
            em.flush();
            
            m.setCode(HttpServletResponse.SC_OK);
            m.setMessage("El usuario se creó correctamente con la clave "
                    +u.getUsuarioid());
            m.setDetail(gson.toJson(u));
            
        } catch(EntityExistsException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST);
            m.setMessage("No se pudo guardar el usuario, intente nuevamente");
            m.setDetail(e.toString());
        }
        return gson.toJson(m);
    }
    public String updateUsuario(int usuarioid) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Message m = new Message();
        
        Usuarios u = em.find(Usuarios.class, usuarioid);
        
        try {
            if(u!=null){
                u.setActivo(!u.getActivo());
                em.merge(u);
            
                m.setCode(HttpServletResponse.SC_OK);
                if(u.getActivo()){
                    m.setMessage("El usuario "+ u.getNombre() +" se activo correctamente");
                }else{
                    m.setMessage("El usuario "+ u.getNombre() +" se desactivo correctamente");
                }
                m.setDetail(gson.toJson(u));
            }else{
                m.setCode(HttpServletResponse.SC_BAD_REQUEST);
                m.setMessage("El usuario no existe");
                m.setDetail("Usuario no encontrado");
            }
            //Crear un nuevo registro en la BD
            
            
        } catch(IllegalArgumentException e) {
            m.setCode(HttpServletResponse.SC_BAD_REQUEST);
            m.setMessage("No se pudo actualizar el usuario, intente nuevamente");
            m.setDetail(e.toString());
        }
        
        return gson.toJson(m);
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
