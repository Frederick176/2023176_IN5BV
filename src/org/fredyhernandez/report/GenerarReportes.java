package org.fredyhernandez.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.fredyhernandez.db.Conexion;

public class GenerarReportes {
     
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametro){
        InputStream reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametro, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}



/*
Interface Map   
    HashMap es uno de los objetos que implementa un conjunto de key-value.
    Tieme un constructor sin parametros new HashMap() y su finalidad suele 
    referirse para agrupar informacion en un unico ebjeto.

Tiene Carta similitud  con la colleccion de objetos ArrayList pero con la diferencia que estos  no tienen un 
orden  

Hash hace referencia a una tecnica de organizacion de archivos el hasing (abierto-cerrado) en la que se almacena 
el registro de una direccion que es generada por una funcion se aplica a la llave del registro dentro de memoria fisica 

En java  el HashMap posee un espacio de memoria y cuando se guarda un objeto en dicho
espacion se determina su direccion, aplicando una fucion a la llalve que le indique.
Cada objeto se identifica mediante algun identificador apropiado


*/
