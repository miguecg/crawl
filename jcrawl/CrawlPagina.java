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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DataNode;
import jcrawl.dbc.Dao;



public class CrawlPagina {
    
    
    

    public static Noticia extraerArchivo(File f) throws Exception {
        
        Noticia noticia = null;
        
        String salida = null;
        List<Noticia> lnt = new ArrayList();
        Document document = Jsoup.parse(f, salida);
        List<Element> le = document.getElementsByTag("article");
        
        for (Element e : le) {
            noticia = new Noticia();
            noticia.setTitulo(e.getElementsByClass("newsTitle").text());
            noticia.setContenido(e.getElementsByClass("notebody").text());
            noticia.setFecha(e.getElementsByClass("time").text());
            noticia.setUrl(f.getName());
            List<String> lst = new ArrayList();
            
            List<Element> lef = e.getElementsByTag("em");
            for (Element es : lef) {
                lst.add(es.text());
            }
            
        }        
        
     return noticia;   
    }
    
    
    public static Noticia extraer(URL url) throws Exception {
        
        Noticia noticia = null;
        
        String salida = null;
        List<Noticia> lnt = new ArrayList();
        Document document = Jsoup.parse(url, 10000);
        List<Element> le = document.getElementsByTag("article");
        
        for (Element e : le) {
            noticia = new Noticia();
            noticia.setTitulo(e.getElementsByClass("newsTitle").text());
            noticia.setContenido(e.getElementsByClass("notebody").text());
            noticia.setFecha(e.getElementsByClass("time").text());
            noticia.setUrl(url.toString());
            List<String> lst = new ArrayList();
            
            List<Element> lef = e.getElementsByTag("em");
            for (Element es : lef) {
                lst.add(es.text());
            }
            
        }        
        
     return noticia;   
    }
    
    
    public static void agrNoticia(Noticia nt) throws Exception {
        
        try {
            Dao dao = new Dao();
            
            
            dao.insertar("Insert into NOTICIA (NOTI_TITULO,NOTI_CONTENIDO,NOTI_URL,NOTI_FECHA) VALUES ('"+nt.getTitulo()+"','"+nt.getContenido()+"','"+nt.getUrl()+"','"+nt.getFecha()+"')");
            /**
            for (String s : nt.getEm()) {
                dao.insertar("Insert into EMFASIS(EMFA_CONTENIDO,EMFA_NOTICIA) VALUES () ");
            }
            */
            
            
            dao.desconectar();;
            
        } catch (SQLException | IOException e) {
            
        }
        
        
    }
   
}
