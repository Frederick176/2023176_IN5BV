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
    private Principal  escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones   = operaciones.NINGUNO;
    private ObservableList <Productos> listarProductos;
    private ObservableList <Proveedores> listarProveedores;
    private ObservableList <TipoProducto> listarTipoProducto;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtcodigoProducto;
    @FXML private TextField txtdescripcionProducto;
    @FXML private TextField txtprecioUnitario;
    @FXML private TextField txtprecioDocena;
    @FXML private TextField txtprecioMayor;
    @FXML private TextField txtexistencia;
    @FXML private TableView tblProductos;
    
    @FXML private ComboBox cmbcodigoTipoProducto;
    @FXML private ComboBox cmbcodigoProveedor;
    
    @FXML private TableColumn colcodigoProducto;
    @FXML private TableColumn coldescripcionProducto;
    @FXML private TableColumn colprecioUnitario;
    @FXML private TableColumn colprecioDocena;
    @FXML private TableColumn colprecioMayor;
    @FXML private TableColumn colexistencia;
    @FXML private TableColumn colcodigoTipoProducto;
    @FXML private TableColumn colcodigoProveedor;
    
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
        cmbcodigoTipoProducto.setItems(getTipoProducto());
        cmbcodigoProveedor.setItems(getProveedores());
        
    }   
    
    public void cargarDatos(){
        tblProductos.setItems(getProductos());
        colcodigoProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("codigoProducto"));
        coldescripcionProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        coldescripcionProducto.setCellValueFactory(new PropertyValueFactory<Productos, String>("descripcionProducto"));
        colprecioUnitario.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioUnitario"));
        colprecioDocena.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioDocena"));
        colprecioMayor.setCellValueFactory(new PropertyValueFactory<Productos, Double>("precioMayor"));
        colexistencia.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("existencia"));
        colcodigoTipoProducto.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoTipoProducto"));
        colcodigoProveedor.setCellValueFactory(new PropertyValueFactory<Productos, Integer>("codigoProveedor"));
        
    }
    
    public void seleccionarElemento(){
        txtcodigoProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
        txtdescripcionProducto.setText(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getDescripcionProducto());
        txtprecioUnitario.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtprecioDocena.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtprecioMayor.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getPrecioMayor()));
        txtexistencia.setText(String.valueOf(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        cmbcodigoTipoProducto.getSelectionModel().select(buscarTipoProducto(((Productos)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        
    }
    
    public TipoProducto buscarTipoProducto (int codigoTipoProducto){
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
            while (resultado.next()){
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
            
        }catch (Exception e){
            e.printStackTrace();
            
        }
        
        return listarProductos = FXCollections.observableArrayList(lista);
        
    }
    
    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> listaProveedores = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                listaProveedores.add(new Proveedores(resultado.getInt("codigoProveedor"),
                            resultado.getString("nitProveedor"),
                            resultado.getString("nombreProveedor"),
                            resultado.getString("apellidoProveedor"),
                            resultado.getString("direccionProveedor"),
                            resultado.getString("razonSocial"),
                            resultado.getString("contactoPrincipal"),
                            resultado.getString("paginaWeb")
                ));
                
            }
            
        }catch (Exception e){
            e.printStackTrace();
            
        }
        
        return listarProveedores = FXCollections.observableList(listaProveedores);
        
    }
    
    public  ObservableList<TipoProducto> getTipoProducto(){
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
             while (resultado.next()) {
                 lista.add(new TipoProducto(resultado.getInt("codigoTipoProducto"),
                            resultado.getString("descripcion")
                 ));
                 
             }
            
        }catch (Exception e){
            e.printStackTrace();
            
        }
        
        return listarTipoProducto = FXCollections.observableList(lista);
        
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
                 imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarProducto.png"));
                 imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarProducto.png"));
                 tipoDeOperaciones = operaciones.NINGUNO;
                 
         }
    }
    
    public void guardar(){
         Productos registro = new Productos();
          registro.setCodigoProducto(txtcodigoProducto.getText());
          registro.setDescripcionProducto(txtdescripcionProducto.getText());
          registro.setPrecioUnitario(Double.parseDouble(txtprecioUnitario.getText()));
          registro.setPrecioDocena(Double.parseDouble(txtprecioDocena.getText()));
          registro.setPrecioMayor(Double.parseDouble(txtprecioMayor.getText()));
          registro.setExistencia(Integer.parseInt(txtexistencia.getText()));
          registro.setCodigoProveedor(((Proveedores)cmbcodigoTipoProducto.getSelectionModel().getSelectedItem()).getCodigoProveedor());
          registro.setCodigoTipoProducto(((TipoProducto)cmbcodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
          
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
              
          }catch (Exception e){
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
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro",
                            "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
    
    public void desactivarControles(){
        txtcodigoProducto.setEditable(false);
        txtdescripcionProducto.setEditable(false);
        txtprecioUnitario.setEditable(false);
        txtprecioDocena.setEditable(false);
        txtprecioMayor.setEditable(false);
        txtexistencia.setEditable(false);
        cmbcodigoTipoProducto.setDisable(true);
        cmbcodigoProveedor.setDisable(true);
        
    }
    
    public void activarControles(){
        txtcodigoProducto.setEditable(true);
        txtdescripcionProducto.setEditable(true);
        txtprecioUnitario.setEditable(true);
        txtprecioDocena.setEditable(true);
        txtprecioMayor.setEditable(true);
        txtexistencia.setEditable(true);
        cmbcodigoTipoProducto.setDisable(false);
        cmbcodigoProveedor.setDisable(false);
        
    }
    
    public void limpiarControles(){
        txtcodigoProducto.clear();
        txtdescripcionProducto.clear();
        txtprecioUnitario.clear();
        txtprecioDocena.clear();
        txtprecioMayor.clear();
        txtexistencia.clear();
        tblProductos.getSelectionModel().getSelectedItem();
        cmbcodigoTipoProducto.getSelectionModel().getSelectedItem();
        cmbcodigoProveedor.getSelectionModel().getSelectedItem();
        
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
        
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
