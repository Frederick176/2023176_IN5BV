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
import org.fredyhernandez.bean.Productos;
import org.fredyhernandez.bean.Proveedores;
import org.fredyhernandez.bean.TipoProducto;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuProductosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Productos> listarProductos;
    private ObservableList<Proveedores> listarProveedores;
    private ObservableList<TipoProducto> listarTipoProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtDescripcionProducto;
    @FXML private TextField txtPrecioUnitario;
    @FXML private TextField txtPrecioDocena;
    @FXML private TextField txtPrecioMayor;
    @FXML private TextField txtExistencia;
    @FXML private TableView tblProductos;
    @FXML private Button btnRegresar;
    
    @FXML private ComboBox cmbCodigoTipoProducto;
    @FXML private ComboBox cmbCodigoProveedor;
    
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colDescripcionProducto;
    @FXML private TableColumn colPrecioUnitario;
    @FXML private TableColumn colPrecioDocena;
    @FXML private TableColumn colPrecioMayor;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colCodigoTipoProducto;
    @FXML private TableColumn colCodigoProveedor;
    
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
        cmbCodigoTipoProducto.setItems(getTipoProducto());
        cmbCodigoProveedor.setItems(getProveedores());
        
    }  
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colPrecioDocena.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colCodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        txtDescripcionProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtPrecioUnitario.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioDocena.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioMayor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtExistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmbCodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        
    }
    
    public TipoProducto buscarTipoProducto(int codigoTipoProducto){
        TipoProducto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoProducto(?)}");
            procedimiento.setInt(1, codigoTipoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoProducto(registro.getInt("codigoTipoProducto"),
                                                             registro.getString("descripcion")
                );
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return resultado;
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
    
    public ObservableList<TipoProducto> getTipoProducto(){
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try{
            PreparedStatement procedimientos = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimientos.executeQuery();
            while(resultado.next()){
                lista.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                                                         resultado.getString("descripcion")
                ));
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return listarTipoProducto = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Proveedores> getProveedores(){
        ArrayList<Proveedores> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedores (resultado.getInt("codigoProveedor"),
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
                    imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarProductos.png"));
                    imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarProductos.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    break;
                    
        }
    }
    
    public void guardar(){
        Productos registro = new Productos();
        registro.setCodigoProducto(txtCodigoProducto.getText());
        registro.setDescripcionProducto(txtDescripcionProducto.getText());
        registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
        registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
        registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
        registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
        registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()); 
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getCodigoTipoProducto());
            procedimiento.setInt(8, registro.getCodigoProveedor());
            procedimiento.execute();
            listarProductos.add(registro);
            
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
                imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarProductos.png"));
                imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarProductos.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmo que se elimine", 
                            "Eliminar Productos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                            procedimiento.setString(1, ((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listarProductos.remove(tblProductos.getSelectionModel().getSelectedItem());
                            
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
               if(tblProductos.getSelectionModel().getSelectedItem() != null){
                   activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtCodigoProducto.setEditable(false);
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
                   imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarProductos.png"));
                   imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesProductos.png"));
                   limpiarControles();
                   tipoDeOperaciones = operaciones.NINGUNO;
                   cargarDatos();
                   break;
                   
       }
   }
   
   public void actualizar(){
       try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarProductos(?, ?, ?, ?, ?, ?, ?, ?)}");
           Productos registro = (Productos)tblProductos.getSelectionModel().getSelectedItem();
           registro.setDescripcionProducto(txtDescripcionProducto.getText());
           registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
           registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioMayor(Double.parseDouble(txtPrecioMayor.getText()));
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setCodigoTipoProducto(((TipoProducto)cmbCodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
            registro.setCodigoProveedor(((Proveedores)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setString(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcionProducto());
            procedimiento.setDouble(3, registro.getPrecioUnitario());
            procedimiento.setDouble(4, registro.getPrecioDocena());
            procedimiento.setDouble(5, registro.getPrecioMayor());
            procedimiento.setInt(6, registro.getExistencia());
            procedimiento.setInt(7, registro.getCodigoTipoProducto());
            procedimiento.setInt(8, registro.getCodigoProveedor());
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
               tipoDeOperaciones = operaciones.NINGUNO;
               break;
               
       }
   }
   
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtDescripcionProducto.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        txtPrecioDocena.setEditable(false);
        txtPrecioMayor.setEditable(false);
        txtExistencia.setEditable(false);
        cmbCodigoTipoProducto.setDisable(true);
        cmbCodigoProveedor.setDisable(true); 
        
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtDescripcionProducto.setEditable(true);
        txtPrecioUnitario.setEditable(true);
        txtPrecioDocena.setEditable(true);
        txtPrecioMayor.setEditable(true);
        txtExistencia.setEditable(true);
        cmbCodigoTipoProducto.setDisable(false);
        cmbCodigoProveedor.setDisable(false); 
        
    }
    
     public void limpiarControles(){
          txtCodigoProducto.clear();
          txtDescripcionProducto.clear();
          txtPrecioUnitario.clear();
          txtPrecioDocena.clear();
          txtPrecioMayor.clear();
          txtExistencia.clear();
          
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
