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
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DataNode;


public class Jcrawl {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
       // String url = "/Users/miguel/devel/crawl/pages/www.mimorelia.com/noticias/morelia/alcalde-de-morelia-reconoce-que-tienen-observaciones-por-falta-de-transparencia/187798";
        String salida = null;
        File f = new File("/Users/miguel/devel/crawl/pages/www.mimorelia.com/noticias/morelia/alcalde-de-morelia-reconoce-que-tienen-observaciones-por-falta-de-transparencia/187798");
      
        Noticia nt = CrawlPagina.extraerArchivo(f);
        
        
        CrawlPagina.agrNoticia(nt);
       
    }

}
