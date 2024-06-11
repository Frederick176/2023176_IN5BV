package org.fredyhernandez.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fredyhernandez.controller.MenuCargoEmpleadosController;
import org.fredyhernandez.controller.MenuClientesController;
import org.fredyhernandez.controller.MenuComprasController;
import org.fredyhernandez.controller.MenuDetalleFacturaController;
import org.fredyhernandez.controller.MenuEmpleadosController;
import org.fredyhernandez.controller.MenuFacturasController;
import org.fredyhernandez.controller.MenuPrincipalController;
import org.fredyhernandez.controller.MenuProductosController;
import org.fredyhernandez.controller.MenuProveedoresController;
import org.fredyhernandez.controller.MenuTipoProductoController;
import org.fredyhernandez.controller.ProgramadorController;

/*Documentacio:
 *Nombre: Fredy Hernandez
 * Fecha creacion; 11/04/2024
 *  Fecha modificacion:
 */

public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String URLVIEW = "/org/fredyhernandez/view/";
    
    /* FXMLLoader, Parent el separador en el explorador de windows es /  */
    
    @Override
    public void start (Stage  escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Kinal Express");
           escenarioPrincipal.getIcons().add(new Image(Principal.class.getResourceAsStream("/org/fredyhernandez/images/supermercado.png")));
           
        menuPrincipalView();   
       /* Parent root = FXMLLoader.load(getClass().getResource("/org/fredyhernandez/view/MenuPrincipalView.fxml"));
        Scene escena = new Scene (root);
        escenarioPrincipal.setScene(escena);*/
        escenarioPrincipal.show();
        
        
    }
    
    public Initializable cambiarEscena(String fxmlName, int width, int heigth) throws Exception{
        Initializable resultado;
        FXMLLoader loader = new FXMLLoader();
        
         InputStream file  =  Principal.class.getResourceAsStream(URLVIEW + fxmlName); 
         loader.setBuilderFactory(new JavaFXBuilderFactory());
         loader.setLocation(Principal.class.getResource(URLVIEW + fxmlName));
         
         escena = new Scene ( (AnchorPane) loader.load(file) , width, heigth );
          escenarioPrincipal.setScene(escena);
          escenarioPrincipal.sizeToScene();
          resultado = (Initializable)loader.getController(); 
        
    return resultado;
    }
    
    public void menuPrincipalView(){
        try{
            MenuPrincipalController menuPrincipalView = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 940, 530);
            menuPrincipalView.setEscenarioPrincipal(this); 
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuClientesView(){
        try{
            MenuClientesController menuClienteView = (MenuClientesController) cambiarEscena ("MenuClientesView.fxml", 1062, 597);
            menuClienteView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void ProgramadorView() {
        try{
            ProgramadorController programadorView = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 830, 630);
            programadorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuProveedoresView(){
        try{
            MenuProveedoresController menuProveedoresView = (MenuProveedoresController) cambiarEscena("MenuProveedoresView.fxml", 1165, 650);
            menuProveedoresView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuComprasView(){
        try{
            MenuComprasController menuComprasView = (MenuComprasController) cambiarEscena("MenuComprasView.fxml", 1080, 610);
            menuComprasView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuCargoEmpleadosView(){
        try{
            MenuCargoEmpleadosController menuCargoEmpleadosView = (MenuCargoEmpleadosController) cambiarEscena("MenuCargoEmpleadosView.fxml", 1135, 636);
            menuCargoEmpleadosView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuTipoProductoView(){
        try{
            MenuTipoProductoController menuTipoProductoView = (MenuTipoProductoController) cambiarEscena("MenuTipoProductoView.fxml", 1080, 617);
            menuTipoProductoView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuProductosView(){
        try{
            MenuProductosController menuProductosView = (MenuProductosController) cambiarEscena ("MenuProductosView.fxml", 1319, 740);
            menuProductosView.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuEmpleadosView(){
        try{
            MenuEmpleadosController menuEmpleadosView = (MenuEmpleadosController) cambiarEscena ("MenuEmpleadosView.fxml", 1251, 704);
            menuEmpleadosView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuFacturasView(){
        try{
            MenuFacturasController menuFacturasView = (MenuFacturasController) cambiarEscena ("MenuFacturasView.fxml", 1218, 684);
            menuFacturasView.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public void menuDetalleFactura(){
        try{
            MenuDetalleFacturaController menuDetalleFacturaView = (MenuDetalleFacturaController) cambiarEscena ("MenuDetalleFacturaView.fxml", 1291, 716);
            menuDetalleFacturaView.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
        
    }
}