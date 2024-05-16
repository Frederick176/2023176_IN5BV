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
import org.fredyhernandez.bean.Clientes;
import org.fredyhernandez.bean.Compras;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuComprasController implements Initializable {
      private Principal escenarioPrincipal;
      private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
      private operaciones tipoDeOperaciones   = operaciones.NINGUNO;
      private ObservableList<Compras> listarCompras;
      
      @FXML private Button btnRegresar;
      @FXML private TextField txtnumeroDoc;
      @FXML private TextField txtfechaDoc;
      @FXML private TextField txtdescripcion;
      @FXML private TextField txttotalDoc;
      @FXML private TableView tblCompras;
      
      @FXML private TableColumn colnumeroDoc;
      @FXML private TableColumn colfechaDoc;
      @FXML private TableColumn coldescripcion;
      @FXML private TableColumn coltotalDoc;
      
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
        tblCompras.setItems(getCompras());
         colnumeroDoc.setCellValueFactory(new PropertyValueFactory<Compras, Integer>("numeroDocumento"));
         colfechaDoc.setCellValueFactory(new PropertyValueFactory<Compras, String>("fechaDocumento"));
         coldescripcion.setCellValueFactory(new PropertyValueFactory<Compras, String>("descripcion"));
         coltotalDoc.setCellValueFactory(new PropertyValueFactory<Compras, Double>("totalDocumento"));
         
    }
    
    public void seleccionarElemento(){
        txtnumeroDoc.setText(String.valueOf(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        txtfechaDoc.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getFechaDocumento());
        txtdescripcion.setText(((Compras)tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
        txttotalDoc.setText(String.valueOf(((Compras) tblCompras.getSelectionModel().getSelectedItem()).getTotalDocumento()));
        
    }
    
     public ObservableList<Compras> getCompras(){
         ArrayList<Compras> lista = new ArrayList<>();
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCompras()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Compras(resultado.getInt("numeroDocumento"),
                                                       resultado.getString("fechaDocumento"),
                                                       resultado.getString("descripcion"),
                                                       resultado.getDouble("totalDocumento")
                 ));
                 
             }
         }catch(Exception e){
             e.printStackTrace();
             
         }
         return listarCompras = FXCollections.observableArrayList(lista);
       
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
                  imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarCompras.png"));
                  imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarCompras.png"));
                  tipoDeOperaciones = operaciones.NINGUNO;
                  
                  }
          }
      
      public void guardar(){
          Compras registro = new Compras();
          registro.setNumeroDocumento(Integer.parseInt(txtnumeroDoc.getText()));  
          registro.setFechaDocumento(txtfechaDoc.getText());
          registro.setDescripcion(txtdescripcion.getText());
          registro.setTotalDocumento(Double.parseDouble(txttotalDoc.getText()));
          
          try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCompras(?, ?, ?, ?)}");
                procedimiento.setInt(1, registro.getNumeroDocumento());
                procedimiento.setString(2, registro.getFechaDocumento());
                procedimiento.setString(3, registro.getDescripcion());
                procedimiento.setDouble(4, registro.getTotalDocumento());
                procedimiento.execute();
                listarCompras.add(registro);
                
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
                      imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarCompras.png"));
                      imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarCompras.png"));
                      tipoDeOperaciones = operaciones.NINGUNO;
                      break;
              default:
                  if(tblCompras.getSelectionModel().getSelectedItem() != null){
                      int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro",
                              "Eliminar Clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                      if (respuesta == JOptionPane.YES_NO_OPTION){
                          try{
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                              procedimiento.setInt(1, ((Compras)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                              procedimiento.execute();
                              listarCompras.remove(tblCompras.getSelectionModel().getSelectedItem());
                              
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
                  if (tblCompras.getSelectionModel().getSelectedItem() != null){
                      activarControles();
                      btnEditar.setText("Actualizar");
                      btnReporte.setText("Cancelar");
                      btnAgregar.setDisable(true);
                      btnEliminar.setDisable(true);
                      imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                      imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                      txtnumeroDoc.setEditable(false);
                      tipoDeOperaciones = operaciones.ACTUALIZAR;
                      
                  }else
                      JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                  break;
                  
              case ACTUALIZAR:
                  desactivarControles();
                  actualizar();
                  btnEditar.setText("Editar");
                  btnReporte.setText("Reportes");
                  btnAgregar.setDisable(false);
                  btnEliminar.setDisable(false);
                  imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarCompras.png"));
                  imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesCompras.png"));
                  limpiarControles();
                  tipoDeOperaciones = operaciones.NINGUNO;
                  cargarDatos();
                  break;
                  
          }
      }
      
      public void actualizar(){
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCompras(?, ?, ?, ?)}");
              Compras registro = (Compras)tblCompras.getSelectionModel().getSelectedItem();
              registro.setNumeroDocumento(Integer.parseInt(txtnumeroDoc.getText()));
              registro.setFechaDocumento(txtfechaDoc.getText());
              registro.setDescripcion(txtdescripcion.getText());
              registro.setTotalDocumento(Double.parseDouble(txttotalDoc.getText()));
              procedimiento.setInt(1, registro.getNumeroDocumento());
              procedimiento.setString(2, registro.getFechaDocumento());
              procedimiento.setString(3, registro.getDescripcion());
              procedimiento.setDouble(4, registro.getTotalDocumento());
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
                  imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarCompras.png"));
                  imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesCompras.png"));
                  tipoDeOperaciones = operaciones.NINGUNO;
                  break;
          }
      }
      
      public void desactivarControles(){
          txtnumeroDoc.setEditable(false);
          txtfechaDoc.setEditable(false);
          txtdescripcion.setEditable(false);
          txttotalDoc.setEditable(false);
          
      }
      
      public void activarControles(){
          txtnumeroDoc.setEditable(true);
          txtfechaDoc.setEditable(true);
          txtdescripcion.setEditable(true);
          txttotalDoc.setEditable(true);
          
      }
      
      public void limpiarControles(){
          txtnumeroDoc.clear();
          txtfechaDoc.clear();
          txtdescripcion.clear();
          txttotalDoc.clear();
          
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
