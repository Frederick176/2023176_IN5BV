package org.fredyhernandez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.fredyhernandez.system.Principal;

public class MenuProductosController implements Initializable {
    private Principal  escenarioPrincipal;
    @FXML private Button btnRegresar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
         this.escenarioPrincipal = escenarioPrincipal;
         
    }
    
    @FXML
    public void regresar (ActionEvent event){
        if (event.getSource() == btnRegresar){
            escenarioPrincipal.menuPrincipalView();
            
        }
    }
}
