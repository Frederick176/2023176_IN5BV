package org.fredyhernandez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.fredyhernandez.bean.Proveedores;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.report.GenerarReportes;
import org.fredyhernandez.system.Principal;

public class MenuProveedoresController implements Initializable {
    private Principal escenarioPrincipal;

    private enum operaciones{AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Proveedores> listarProveedores;
    
    @FXML private Button btnRegrear;
    @FXML private TextField txtCodigoP;
    @FXML private TextField txtNit;
    @FXML private TextField txtNombreP;
    @FXML private TextField txtApellidoP;
    @FXML private TextField txtDireccionP;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtContactoPrincipal;
    @FXML private TextField txtPaginaWeb;
    @FXML private TableView tblProveedores;
    
    @FXML private TableColumn colCodigoP;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colNombreP;
    @FXML private TableColumn colApellidoP;
    @FXML private TableColumn colDireccionP;
    @FXML private TableColumn colRazonSocial;
    @FXML private TableColumn colContactoPrincipal;
    @FXML private TableColumn colPaginaWeb;
    
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
        tblProveedores.setItems(getProveedores());
         colCodigoP.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("codigoProveedor"));
         colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nitProveedor"));
         colNombreP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nombreProveedor"));
         colApellidoP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("apellidoProveedor"));
         colDireccionP.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccionProveedor"));
         colRazonSocial.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("razonSocial"));
         colContactoPrincipal.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("contactoPrincipal"));
         colPaginaWeb.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("paginaWeb"));
         
    }
    
    public void seleccionarElemento(){
         txtCodigoP.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
         txtNit.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNitProveedor());
         txtNombreP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getNombreProveedor());
         txtApellidoP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getApellidoProveedor());
         txtDireccionP.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getDireccionProveedor());
         txtRazonSocial.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
         txtContactoPrincipal.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
         txtPaginaWeb.setText(((Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
         
    }
   
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                    lista.add(new Proveedores(resultado.getInt("codigoProveedor"),
                                                            resultado.getString("nitProveedor"),
                                                            resultado.getString("nombreProveedor"),
                                                            resultado.getString("apellidoProveedor"),
                                                            resultado.getString("direccionProveedor"),
                                                            resultado.getString("razonSocial"),
                                                            resultado.getString("contactoPrincipal"),
                                                            resultado.getString("paginaWeb")
                    ));
                    
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listarProveedores = FXCollections.observableArrayList(lista);
        
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
                   imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarProveedores.png"));
                   imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarProveedores.png"));
                   tipoDeOperaciones = operaciones.NINGUNO;
                   
          }     
     }
     
     public void guardar(){
          Proveedores registro = new Proveedores();
           registro.setCodigoProveedor(Integer.parseInt(txtCodigoP.getText()));
           registro.setNitProveedor(txtNit.getText());
           registro.setNombreProveedor(txtNombreP.getText());
           registro.setApellidoProveedor(txtApellidoP.getText());
           registro.setDireccionProveedor(txtDireccionP.getText());
           registro.setRazonSocial(txtRazonSocial.getText());
           registro.setContactoPrincipal(txtContactoPrincipal.getText());
           registro.setPaginaWeb(txtPaginaWeb.getText());
           
           try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
               procedimiento.setInt(1, registro.getCodigoProveedor());
               procedimiento.setString(2, registro.getNitProveedor());
               procedimiento.setString(3, registro.getNombreProveedor());
               procedimiento.setString(4, registro.getApellidoProveedor());
               procedimiento.setString(5, registro.getDireccionProveedor());
               procedimiento.setString(6, registro.getRazonSocial());
               procedimiento.setString(7, registro.getContactoPrincipal());
               procedimiento.setString(8, registro.getPaginaWeb());
               procedimiento.execute();
               listarProveedores.add(registro);
                
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
                  imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarProveedores.png"));
                  imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarProveedores.png"));
                  tipoDeOperaciones = operaciones.NINGUNO;
                  break;
             default:
                 if(tblProveedores.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro",
                             "Eliminar Proveedores", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if (respuesta == JOptionPane.YES_NO_OPTION){
                         try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProveedores(?)}");
                             procedimiento.setInt(1, (( Proveedores)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                             procedimiento.execute();
                             listarProveedores.remove(tblProveedores.getSelectionModel().getSelectedItem());
                             
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
                 if (tblProveedores.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                    imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                    txtCodigoP.setEditable(false);
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
                    imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarProveedores.png"));
                    imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesProveedores.png"));
                    limpiarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                  break;
                    
         }
     }
     
     public void actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProveedores(?, ?, ?, ?, ?, ?, ?, ?)}");
             Proveedores registro = (Proveedores)tblProveedores.getSelectionModel().getSelectedItem();
              registro.setNitProveedor(txtNit.getText());
              registro.setNombreProveedor(txtNombreP.getText());
              registro.setApellidoProveedor(txtApellidoP.getText());
              registro.setDireccionProveedor(txtDireccionP.getText());
              registro.setRazonSocial(txtRazonSocial.getText());
              registro.setContactoPrincipal(txtContactoPrincipal.getText());
              registro.setPaginaWeb(txtPaginaWeb.getText());
              procedimiento.setInt(1, registro.getCodigoProveedor());
              procedimiento.setString(2, registro.getNitProveedor());
              procedimiento.setString(3, registro.getNombreProveedor());
              procedimiento.setString(4, registro.getApellidoProveedor());
              procedimiento.setString(5, registro.getDireccionProveedor());
              procedimiento.setString(6, registro.getRazonSocial());
              procedimiento.setString(7, registro.getContactoPrincipal());
              procedimiento.setString(8, registro.getPaginaWeb());
              procedimiento.execute();
              
         }catch(Exception e){
             e.printStackTrace();
             
         }
     }
     
     public void reporte(){
         switch(tipoDeOperaciones){
             case NINGUNO:
                 imprimirReporte();
                 break;
             case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarProveedores.png"));
                imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesProveedores.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
         }
     }
     
     public void imprimirReporte(){
         Map parametros = new HashMap();
          parametros.put("codigoProveedor", null);
          GenerarReportes.mostrarReportes("ReporteProveedores.jasper", "Reporte de los Proveedores", parametros);
     }
    
    public void desactivarControles(){
        txtCodigoP.setEditable(false);
        txtNit.setEditable(false);
        txtNombreP.setEditable(false);
        txtApellidoP.setEditable(false);
        txtDireccionP.setEditable(false);
        txtRazonSocial.setEditable(false);
        txtContactoPrincipal.setEditable(false);
        txtPaginaWeb.setEditable(false);
        
    }
    
    public void activarControles(){
        txtCodigoP.setEditable(true);
        txtNit.setEditable(true);
        txtNombreP.setEditable(true);
        txtApellidoP.setEditable(true);
        txtRazonSocial.setEditable(true);
        txtContactoPrincipal.setEditable(true);
        txtPaginaWeb.setEditable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoP.clear();
        txtNit.clear();
        txtNombreP.clear();
        txtApellidoP.clear();
        txtRazonSocial.clear();
        txtContactoPrincipal.clear();
        txtPaginaWeb.clear();
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
        
    }
    
    @FXML
    public void regresar(ActionEvent event){
        if(event.getSource() == btnRegrear){
            escenarioPrincipal.menuPrincipalView();
        }
    }
    
}
