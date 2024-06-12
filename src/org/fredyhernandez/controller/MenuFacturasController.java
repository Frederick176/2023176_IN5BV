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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.fredyhernandez.bean.Clientes;
import org.fredyhernandez.bean.Empleados;
import org.fredyhernandez.bean.Facturas;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuFacturasController implements Initializable {
    private Principal escenarioPrincipal;

    private ObservableList getFacturas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Facturas> listarFacturas;
    private ObservableList<Clientes> listarClientes;
    private ObservableList<Empleados> listarEmpleados;
    
    @FXML private TextField txtnumeroFactura;
    @FXML private TextField txtEstado;
    @FXML private TextField txtTotalFactura;
    @FXML private TextField txtFechaFactura;
    @FXML private TableView tblFactura;
    
    @FXML private ComboBox cmbCodigoCliente;
    @FXML private ComboBox cmbCodigoEmpleado;
    
    @FXML private TableColumn colNumeroFactura;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colTotalFactura;
    @FXML private TableColumn colFechaFactura;
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colCodigoEmpleado;
    
     @FXML private Button btnAgregar;
     @FXML private Button btnEliminar;
     @FXML private Button btnEditar;
     @FXML private Button btnReporte;
     @FXML private Button btnRegresar;
    
    @FXML private ImageView imagAgregar;
    @FXML private ImageView imagEliminar;
    @FXML private ImageView imagEditar;
    @FXML private ImageView imagReporte;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        cmbCodigoCliente.setItems(getClientes());
        cmbCodigoEmpleado.setItems(getEmpleados());
        
    }   
    
    public void cargarDatos(){
        tblFactura.setItems(getFactura());
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("numeroFactura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Facturas, String>("estado"));
        colTotalFactura.setCellValueFactory(new PropertyValueFactory<Facturas, Double>("totalFactura"));
        colFechaFactura.setCellValueFactory(new PropertyValueFactory<Facturas, String>("fechaFactura"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("codigoCliente"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Facturas, Integer>("codigoEmpleado"));
        
    }
    
    public void seleccionarElemento(){
        txtnumeroFactura.setText(String.valueOf(((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        txtEstado.setText(((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getEstado());
        txtTotalFactura.setText(String.valueOf(((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getTotalFactura()));
        txtFechaFactura.setText(((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getFechaFactura());
        cmbCodigoCliente.getSelectionModel().select(buscarClientes(((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleados(((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
    }
    
    public Clientes buscarClientes(int codigoCliente){
        Clientes resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarClientes(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Clientes(registro.getInt("codigoCliente"),
                                                        registro.getString("nitcliente"),
                                                        registro.getString("nombreCliente"),
                                                        registro.getString("apellidoCliente"),
                                                        registro.getString("direccionCliente"),
                                                        registro.getString("telefonoCliente"),
                                                        registro.getString("correoCliente")
                );
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return resultado;
    }
    
    public Empleados buscarEmpleados(int codigoEmpleados){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            procedimiento.setInt(1, codigoEmpleados);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleados(registro.getInt("codigoEmpleado"),
                                                                registro.getString("nombreEmpleado"),
                                                                registro.getString("apellidoEmpleado"),
                                                                registro.getDouble("sueldo"),
                                                                registro.getString("direccion"),
                                                                registro.getString("turno"),
                                                                registro.getInt("codigoCargoEmpleado")
                );
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return resultado;
        
    }
    
     public ObservableList<Facturas> getFactura(){
        ArrayList<Facturas> lista = new ArrayList<Facturas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call  sp_ListarFacturas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Facturas(resultado.getInt("numeroFactura"),
                                                    resultado.getString("estado"),
                                                    resultado.getDouble("totalFactura"),
                                                    resultado.getString("fechaFactura"),
                                                    resultado.getInt("codigoCliente"),
                                                    resultado.getInt("codigoEmpleado")
                ));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return listarFacturas = FXCollections.observableArrayList(lista);
    }
     
     public ObservableList<Clientes> getClientes(){
         ArrayList<Clientes> lista = new ArrayList<>();
         try{
             PreparedStatement procedimientos = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
             ResultSet resultado = procedimientos.executeQuery();
             while(resultado.next()){
                 lista.add(new Clientes (resultado.getInt("codigoCliente"),
                                                     resultado.getString("NITcliente"),
                                                     resultado.getString("nombreCliente"),
                                                     resultado.getString("apellidoCliente"),
                                                     resultado.getString("direccionCliente"),
                                                     resultado.getString("telefonoCliente"),
                                                     resultado.getString("correoCliente")
                 ));
                 
             }
             
         }catch(Exception e){
             e.printStackTrace();
             
         }
         
         return  listarClientes = FXCollections.observableArrayList(lista);
     }
     
     public ObservableList<Empleados> getEmpleados(){
         ArrayList<Empleados> lista = new ArrayList<Empleados>();
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                                                         resultado.getString("nombreEmpleado"),
                                                         resultado.getString("apellidoEmpleado"),
                                                         resultado.getDouble("sueldo"),
                                                         resultado.getString("direccion"),
                                                         resultado.getString("turno"),
                                                         resultado.getInt("codigoCargoEmpleado")
                 ));
                 
             }
             
         }catch(Exception e){
             e.printStackTrace();
             
         }
         
         return listarEmpleados  = FXCollections.observableArrayList(lista);
     }
     
     public void agregar(){
          switch(tipoDeOperaciones){
              case NINGUNO:
                  activarControles();  
                  btnAgregar.setText("Guardar");
                  btnEliminar.setText("Cancelar");
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
                      imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarGeneral.png"));
                      imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarGeneral.png"));
                      tipoDeOperaciones = operaciones.NINGUNO;
                      cargarDatos();
                      break;
          }
     }
     
     public void guardar(){
         Facturas registro = new Facturas();
         registro.setNumeroFactura(Integer.parseInt(txtnumeroFactura.getText()));
         registro.setEstado(txtEstado.getText());
         registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
         registro.setFechaFactura(txtFechaFactura.getText());
         registro.setCodigoCliente(((Clientes)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
         registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarFacturas(?, ?, ?, ?, ?, ?)}");
             procedimiento.setInt(1, registro.getNumeroFactura());
             procedimiento.setString(2, registro.getEstado());
             procedimiento.setDouble(3, registro.getTotalFactura());
             procedimiento.setString(4, registro.getFechaFactura());
             procedimiento.setInt(5, registro.getCodigoCliente());
             procedimiento.setInt(6, registro.getCodigoEmpleado());
             procedimiento.execute();
             listarFacturas.add(registro);
             
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
                 imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarGeneral.png"));
                 imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarGeneral.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;
             default:
                 if(tblFactura.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar la eliminacion",
                             "Eliminar Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if(respuesta == JOptionPane.YES_NO_OPTION){
                         try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarFacturas(?)}");
                             procedimiento.setInt(1, ((Facturas)tblFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
                             procedimiento.execute();
                             listarFacturas.remove(tblFactura.getSelectionModel().getSelectedItem());
                             
                         }catch(Exception e){
                             e.printStackTrace();
                         }
                     }
                             
                 }else
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
         }
     }
     
     public void editar(){
         switch(tipoDeOperaciones){
             case NINGUNO:
                 if(tblFactura.getSelectionModel().getSelectedItem() != null){
                     activarControles();
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnAgregar.setDisable(true);
                     btnEliminar.setDisable(true);
                     imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                     imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                     txtnumeroFactura.setEditable(false);
                     tipoDeOperaciones = operaciones.ACTUALIZAR;
                 }else
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
                 break;
                 
                 case ACTUALIZAR:
                     desactivarControles();
                     Actualizar();
                     btnEditar.setText("Editar");
                     btnReporte.setText("Reporte");
                     btnAgregar.setDisable(false);
                     btnEliminar.setDisable(false);
                     imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarGeneral.png"));
                     imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesGeneral.png"));
                     limpiarControles();
                     tipoDeOperaciones = operaciones.NINGUNO;
                     cargarDatos();
                     break;          
         }
     }
     
     public void Actualizar(){
         try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarFacturas(?, ?, ?, ?, ?, ?)}");
             Facturas registro = (Facturas)tblFactura.getSelectionModel().getSelectedItem();
             registro.setEstado(txtEstado.getText());
             registro.setTotalFactura(Double.parseDouble(txtTotalFactura.getText()));
             registro.setFechaFactura(txtFechaFactura.getText());
             registro.setCodigoCliente(((Clientes)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
             registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
             procedimiento.setInt(1, registro.getNumeroFactura());
             procedimiento.setString(2, registro.getEstado());
             procedimiento.setDouble(3, registro.getTotalFactura());
             procedimiento.setString(4, registro.getFechaFactura());
             procedimiento.setInt(5, registro.getCodigoCliente());
             procedimiento.setInt(6, registro.getCodigoEmpleado());
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
                 imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarGeneral.png"));
                 imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesGeneral.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 break;
         }
     }
     
     public void desactivarControles(){
         txtnumeroFactura.setEditable(false);
         txtEstado.setEditable(false);
         txtTotalFactura.setEditable(false);
         txtFechaFactura.setEditable(false);
         cmbCodigoCliente.setDisable(true);
         cmbCodigoEmpleado.setDisable(true); 
         
     }
     
     public void activarControles(){
         txtnumeroFactura.setEditable(true);
         txtEstado.setEditable(true);
         txtTotalFactura.setEditable(true);
         txtFechaFactura.setEditable(true);
         cmbCodigoCliente.setDisable(false);
         cmbCodigoEmpleado.setDisable(false); 
         
     }
     
     public void limpiarControles(){
         txtnumeroFactura.clear();
         txtEstado.clear();
         txtTotalFactura.clear();
         txtFechaFactura.clear();
         cmbCodigoCliente.getSelectionModel().getSelectedItem();
         cmbCodigoEmpleado.getSelectionModel().getSelectedItem();
         
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
