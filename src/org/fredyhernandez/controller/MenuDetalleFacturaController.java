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
import org.fredyhernandez.bean.DetalleFactura;
import org.fredyhernandez.bean.Facturas;
import org.fredyhernandez.bean.Productos;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuDetalleFacturaController implements Initializable {
    private Principal escenarioPrincipal;
     private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
     private operaciones tipoDeOperaciones = operaciones.NINGUNO;
     private ObservableList<DetalleFactura> listarDetalleFactura;
     private ObservableList<Facturas> listarFacturas;
     private ObservableList<Productos> listarProductos;
     
     @FXML private TextField txtCodigoDetalleFactura ;
     @FXML private TextField txtPrecioUnitario;
     @FXML private TextField txtCantidad;
     @FXML private TableView tblDetalleFactura;
     @FXML private Button btnRegresar;
    
     @FXML private ComboBox cmbNumeroFactura;
     @FXML private ComboBox cmbCodigoProducto;
     
     @FXML private TableColumn colCodigoDetalleFactura;
     @FXML private TableColumn colPrecioUnitario;
     @FXML private TableColumn colCantidad;
     @FXML private TableColumn colNumeroFactura;
     @FXML private TableColumn colCodigoProducto;
     
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
        cmbNumeroFactura.setItems(getFacturas());
        cmbCodigoProducto.setItems(getProductos());
        
    }   
    
    public void cargarDatos(){
        tblDetalleFactura.setItems(getDetalleFacturas());
        colCodigoDetalleFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("codigoDetalleFactura"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("cantidad"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("numeroFactura"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("codigoProducto"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoDetalleFactura.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
        txtPrecioUnitario.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtCantidad.setText(String.valueOf(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCantidad()));
        cmbNumeroFactura.getSelectionModel().select(buscarFacturas(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        cmbCodigoProducto.getSelectionModel().select(buscarProductos(((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        
    }
    
    public Facturas buscarFacturas(int numeroFactura){
        Facturas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarFacturas(?)}");
            procedimiento.setInt(1, numeroFactura);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Facturas(registro.getInt("numeroFactura"),
                                                      registro.getString("estado"),
                                                      registro.getDouble("totalFactura"),
                                                      registro.getString("fechaFactura"),
                                                      registro.getInt("codigoCliente"),
                                                      registro.getInt("codigoEmpleado")
                );
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return resultado;
    }
    
    public Productos buscarProductos(String codigoProducto){
        Productos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setString(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(registro.getString("codigoProducto"),
                                                        registro.getString("descripcionProducto"),
                                                        registro.getDouble("precioUnitario"),
                                                        registro.getDouble("precioDocena"),
                                                        registro.getDouble("precioMayor"),
                                                        registro.getInt("existencia"),
                                                        registro.getInt("codigoTipoProducto"),
                                                        registro.getInt("codigoProveedor")
                
                );
                
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return resultado;
    }
    
    public ObservableList<DetalleFactura> getDetalleFacturas(){
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleFactura()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleFactura(resultado.getInt("codigoDetalleFactura"),
                                                           resultado.getDouble("precioUnitario"),
                                                           resultado.getInt("cantidad"),
                                                           resultado.getInt("numeroFactura"),
                                                           resultado.getString("codigoProducto")
                ));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return listarDetalleFactura = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Facturas> getFacturas(){
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
    
    public ObservableList<Productos> getProductos(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos (resultado.getString("codigoProducto"),
                                                     resultado.getString("descripcionProducto"),
                                                     resultado.getDouble("precioUnitario"),
                                                     resultado.getDouble("precioDocena"),
                                                     resultado.getDouble("precioMayor"),
                                                     resultado.getInt("existencia"),
                                                     resultado.getInt("codigoTipoProducto"),
                                                     resultado.getInt("codigoProveedor")
                ));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return listarProductos = FXCollections.observableArrayList(lista);
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
        DetalleFactura registro = new DetalleFactura();
        registro.setCodigoDetalleFactura(Integer.parseInt(txtCodigoDetalleFactura.getText()));
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNumeroFactura(((Facturas)cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
        registro.setCodigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleFactura(?, ?, ?, ?, ?)}");
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
            procedimiento.execute();
            listarDetalleFactura.add(registro);
            
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
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmo que se elimine", 
                            "Eliminar Detalle Factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleFactura(?)}");
                            procedimiento.setInt(1, ((DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                            procedimiento.execute();
                            listarDetalleFactura.remove(tblDetalleFactura.getSelectionModel().getSelectedItem());
                              
                        }catch(Exception e){
                            e.printStackTrace();
                            
                        }
                    }   
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un dato para eliminar");
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
        
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblDetalleFactura.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtCodigoDetalleFactura.setEditable(false);
                    imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                    imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un dato para editar");
                break;
                
                case ACTUALIZAR:
                    desactivarControles();  
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reportes");
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
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleFactura(?, ?, ?, ?, ?)}");
            DetalleFactura registro = (DetalleFactura)tblDetalleFactura.getSelectionModel().getSelectedItem();
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNumeroFactura(((Facturas)cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            registro.setCodigoProducto(((Productos)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setDouble(2, registro.getPrecioUnitario());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.setInt(4, registro.getNumeroFactura());
            procedimiento.setString(5, registro.getCodigoProducto());
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
        txtCodigoDetalleFactura.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtCantidad.setEditable(false);
        cmbNumeroFactura.setDisable(true);
        cmbCodigoProducto.setDisable(true); 
        
    }
    
    public void activarControles(){
        txtCodigoDetalleFactura.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtCantidad.setEditable(true);
        cmbNumeroFactura.setDisable(false);
        cmbCodigoProducto.setDisable(false); 
        
    }
    
    public void limpiarControles(){
        txtCodigoDetalleFactura.clear();
        txtPrecioUnitario.clear();
        txtCantidad.clear();
        cmbNumeroFactura.getSelectionModel().getSelectedItem();
        cmbCodigoProducto.getSelectionModel().getSelectedItem();
        
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
