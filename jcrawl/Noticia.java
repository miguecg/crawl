/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcrawl;

/**
 *
 * @author miguel
 */
import java.io.Serializable;
import java.util.List;



public class Noticia implements Serializable {
    
    private String titulo;
    private String contenido;
    private String url;
    private String fecha;
    private List<String> em;

    public void setEm(List<String> em) {
        this.em = em;
    }

    public List<String> getEm() {
        return em;
    }
    
    
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
