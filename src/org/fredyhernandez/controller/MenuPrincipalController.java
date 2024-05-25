
package org.fredyhernandez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.fredyhernandez.system.Principal;

/*Herencia Multiple concepto, Interfaces. POO*/


public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    
    // FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnMenuClientes;
    @FXML MenuItem btnMenuProgramador;
    @FXML MenuItem btnMenuProveedores;
    @FXML MenuItem btnMenuCargoEmpleados;
    @FXML MenuItem btnMenuCompras;
    @FXML MenuItem btnMenuTipoProducto;
    @FXML MenuItem btnMenuProductos;
    @FXML MenuItem btnMenuEmpleados;
    @FXML MenuItem btnMenuFacturas;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    @FXML
    public void clicClientes (ActionEvent event){
        if(event.getSource() == btnMenuClientes){
            escenarioPrincipal.menuClientesView();
            
        }
   }
    
    @FXML
    public void clicPro (ActionEvent event){
        if(event.getSource() == btnMenuProgramador){
            escenarioPrincipal.ProgramadorView();
        }
    }
    
    @FXML
    public void clicProveedores(ActionEvent event){
        if(event.getSource() == btnMenuProveedores){
            escenarioPrincipal.menuProveedoresView();
        }
    }
    
        @FXML 
    public void clicCargoEmpleados(ActionEvent event){
        if(event.getSource() == btnMenuCargoEmpleados){
             escenarioPrincipal.menuCargoEmpleadosView();
        }
    }
    
    @FXML
    public void clicCompras(ActionEvent event){
        if(event.getSource() == btnMenuCompras){
            escenarioPrincipal.menuComprasView();
        }
    }
    
    @FXML 
    public void clicTipoProducto(ActionEvent event){
        if(event.getSource() == btnMenuTipoProducto){
            escenarioPrincipal.menuTipoProductoView();
        }
    }
    
    @FXML 
    public void clicProductos(ActionEvent event){
        if(event.getSource() == btnMenuProductos){
            escenarioPrincipal.menuProductosView();
        }
    }
    
    @FXML
    public void clicEmpleados(ActionEvent event){
        if(event.getSource() == btnMenuEmpleados){
            escenarioPrincipal.menuEmpleadosView();
            
        }
    }
    
    @FXML 
    public void clicFacturas(ActionEvent event){
        if(event.getSource() == btnMenuFacturas){
            escenarioPrincipal.menuFacturasView();
            
        }
    }
}
