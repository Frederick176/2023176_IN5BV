package org.fredyhernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.fredyhernandez.bean.CargoEmpleados;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuCargoEmpleadosController implements Initializable {
        private Principal escenarioPrincipal;
        private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
        private operaciones tipoDeOperaciones   = operaciones.NINGUNO;
        private ObservableList<CargoEmpleados> listarCargoEmpleados;
        
        
      @FXML private Button btnRegresar;
      @FXML private TextField txtCodigoCE;
      @FXML private TextField txtNombreCE;
      @FXML private TextField txtDescripcionC;
      @FXML private TableView tblCargoEmpleados;
      
      @FXML private TableColumn colCodigoCE;
      @FXML private TableColumn colNombreCE;
      @FXML private TableColumn colDescripcionCE;
      
       @FXML private Button btnAgregar;
       @FXML private Button btnEliminar;
       @FXML private Button btnEditar;
       @FXML private Button btnReporte;
       
       @FXML private ImageView imagAgregar;
       @FXML private ImageView imagEliminar;
       @FXML private ImageView imagEditar;
       @FXML private ImageView imagReporte;
      
       
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();  
    }    
    
    public void cargarDatos(){
        tblCargoEmpleados.setItems(getCargoEmpleados());
        colCodigoCE.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, Integer>("codigoCargoEmpleado"));
        colNombreCE.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, String>("nombreCargo"));
        colDescripcionCE.setCellValueFactory(new PropertyValueFactory<CargoEmpleados, String>("descripcionCargo"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoCE.setText(String.valueOf(((CargoEmpleados) tblCargoEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        txtNombreCE.setText(String.valueOf(((CargoEmpleados) tblCargoEmpleados.getSelectionModel().getSelectedItem()).getNombreCargo()));
        txtDescripcionC.setText(String.valueOf(((CargoEmpleados) tblCargoEmpleados.getSelectionModel().getSelectedItem()).getDescripcionCargo()));
        
    }
    
     public ObservableList<CargoEmpleados> getCargoEmpleados(){
         ArrayList<CargoEmpleados> lista = new ArrayList<>();
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleados()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new CargoEmpleados(resultado.getInt("codigoCargoEmpleado"),
                                                                    resultado.getString("nombreCargo"),
                                                                    resultado.getString("descripcionCargo")
                         ));
                 
             }
         }catch(Exception e){
             e.printStackTrace();
         }
         return listarCargoEmpleados = FXCollections.observableArrayList(lista);
         
     }
     
     public void agregar(){
         switch(tipoDeOperaciones){
             case NINGUNO:
                 activarControles();
                 btnAgregar.setText("Guardar");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 imagAgregar.setImage(new Image("/org/fredyhernandez/images/Guardar.png"));
                 imagEliminar.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                 tipoDeOperaciones = operaciones.ACTUALIZAR;
                 break;
                 
                 case ACTUALIZAR:
                     guardar();
                     desactivarControles();
                     limpiarControles();
                     btnAgregar.setText("Agregar");
                     btnEliminar.setText("Eliminar");
                     btnEditar.setDisable(false);
                     btnReporte.setDisable(false);
                     imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarCargoEmpleados.png"));
                     imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarCargoEmpleados.png"));
                     tipoDeOperaciones = operaciones.NINGUNO;
                     
         }
     }
     
     public void guardar(){
         CargoEmpleados registro = new CargoEmpleados();
         registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodigoCE.getText()));
         registro.setNombreCargo(txtNombreCE.getText());
         registro.setDescripcionCargo(txtDescripcionC.getText());
         
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargoEmpleados(?, ?, ?)}");
              procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
              procedimiento.setString(2, registro.getNombreCargo());
              procedimiento.setString(3, registro.getDescripcionCargo());
              procedimiento.execute();
              listarCargoEmpleados.add(registro);
             
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void eliminar(){
         switch(tipoDeOperaciones){
             case ACTUALIZAR:
                 desactivarControles();
                 limpiarControles();
                 btnAgregar.setText("Agregar");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);
                 imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarCargoEmpleados.png"));
                 imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarCargoEmpleados.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;
             default:
                 if(tblCargoEmpleados.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro",
                             "Eliminar CargoEmpleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if (respuesta == JOptionPane.YES_NO_OPTION){
                          try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargoEmpleados(?)}");
                              procedimiento.setInt(1, ((CargoEmpleados)tblCargoEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
                              procedimiento.execute();
                              listarCargoEmpleados.remove(tblCargoEmpleados.getSelectionModel().getSelectedItem());
                              
                          }catch(Exception e){
                              e.printStackTrace();
                          }
                      }
                 }else
                     JOptionPane.showMessageDialog(null, "Debe de seleccionar un Elemento");
         }
     }
     
     public void editar(){
         switch(tipoDeOperaciones){
             case NINGUNO:
                 if (tblCargoEmpleados.getSelectionModel().getSelectedItem() != null){
                     activarControles();
                     btnEditar.setText("Editar");
                     btnReporte.setText("Cancelar");
                     btnAgregar.setDisable(true);
                     btnEliminar.setDisable(true);
                     imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                     imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                     txtCodigoCE.setEditable(false);
                     tipoDeOperaciones = operaciones.ACTUALIZAR;
                     
                 }else
                     JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                 break;
                 
             case ACTUALIZAR:
                 desactivarControles();
                 actualizar();
                 btnEditar.setText("Actualizar");
                 btnReporte.setText("Reportes");
                 btnAgregar.setDisable(false);
                 btnEliminar.setDisable(false);
                 imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarCargoEmpleados.png"));
                 imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesCargoEmpleados.png"));
                 limpiarControles();
                 tipoDeOperaciones = operaciones.NINGUNO;
                 cargarDatos();
                 break;
                 
         }
     }
     
     public void actualizar(){
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargoEmpleados(?, ?, ?)}");
              CargoEmpleados registro = (CargoEmpleados)tblCargoEmpleados.getSelectionModel().getSelectedItem();
              registro.setCodigoCargoEmpleado(Integer.parseInt(txtCodigoCE.getText()));
              registro.setNombreCargo(txtNombreCE.getText());
              registro.setDescripcionCargo(txtDescripcionC.getText());
              procedimiento.setInt(1, registro.getCodigoCargoEmpleado());
              procedimiento.setString(2, registro.getNombreCargo());
              procedimiento.setString(3, registro.getDescripcionCargo());
              procedimiento.execute();
               
          }catch(Exception e){
              e.printStackTrace();
          }
     }
     
     public void reporte(){
          switch(tipoDeOperaciones){
              case ACTUALIZAR:
                  desactivarControles();
                  limpiarControles();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reporte");
                  btnAgregar.setDisable(false);
                  btnEliminar.setDisable(false);
                  imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarCargoEmpleados.png"));
                  imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesCargoEmpleados.png"));
                  tipoDeOperaciones = operaciones.NINGUNO;
                  break;
          }
     }
     
     
     
     public void desactivarControles(){
          txtCodigoCE.setEditable(false);
          txtNombreCE.setEditable(false);
          txtDescripcionC.setEditable(false);
          
     }
     
     public void activarControles(){
          txtCodigoCE.setEditable(true);
          txtNombreCE.setEditable(true);
          txtDescripcionC.setEditable(true);
          
     }
     
     public void limpiarControles(){
         txtCodigoCE.clear();
         txtNombreCE.clear();
         txtDescripcionC.clear();
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
