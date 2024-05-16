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
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;


public class MenuClientesController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones   = operaciones.NINGUNO;
    private ObservableList<Clientes> listarClientes;
    
    @FXML private Button btnRegresar;
    @FXML private TextField txtDireccionC;
    @FXML private TextField txtCorreoC;
    @FXML private TextField txtCodigoC;
    @FXML private TextField txtNit;
    @FXML private TextField txtNombreC;
    @FXML private TextField txtApellidoC;
    @FXML private TextField txtTelefonoC;
    @FXML private TableView tblClientes;
    
    @FXML private TableColumn colCodigoC;
    @FXML private TableColumn colNombreC;
    @FXML private TableColumn colApellidoC;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colDireccionC;
    @FXML private TableColumn colTelefonoC;
    @FXML private TableColumn colCorreoC;
    
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
        tblClientes.setItems(getClientes());
        colCodigoC.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoCliente"));
        colNit.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nitCliente"));
        colNombreC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colDireccionC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colTelefonoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colCorreoC.setCellValueFactory(new PropertyValueFactory<Clientes, String>("CorreoCliente"));
    }
    
    public void seleccionarElemento(){
        txtCodigoC.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        txtNit.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNitCliente());
        txtNombreC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getNombreCliente());
        txtApellidoC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getApellidoCliente());
        txtDireccionC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getDireccionCliente()); 
        txtTelefonoC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getTelefonoCliente()); 
        txtCorreoC.setText(((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCorreoCliente()); 
    }
    
    public ObservableList<Clientes> getClientes(){
        ArrayList<Clientes> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarClientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                    lista.add(new Clientes(resultado.getInt("codigoCliente"),
                                                        resultado.getString("nitCliente"),
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
           
     return listarClientes = FXCollections.observableArrayList(lista);
     
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
                imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarClientes.png"));
                imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarClientes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
        }
    }
    
    public void guardar(){
        Clientes registro = new Clientes();
        registro.setCodigoCliente(Integer.parseInt(txtCodigoC.getText()));  
        registro.setNitCliente(txtNit.getText());
        registro.setNombreCliente(txtNombreC.getText());
        registro.setApellidoCliente(txtApellidoC.getText());
        registro.setDireccionCliente(txtDireccionC.getText());
        registro.setTelefonoCliente(txtTelefonoC.getText());
        registro.setCorreoCliente(txtCorreoC.getText());
            
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarClientes(?, ?, ?, ?, ?, ?, ?)}");
             procedimiento.setInt(1, registro.getCodigoCliente());
             procedimiento.setString(2, registro.getNitCliente());
             procedimiento.setString(3, registro.getNombreCliente());
             procedimiento.setString(4, registro.getApellidoCliente());
             procedimiento.setString(5, registro.getDireccionCliente());
             procedimiento.setString(6, registro.getTelefonoCliente());
             procedimiento.setString(7, registro.getCorreoCliente());
             procedimiento.execute();
             listarClientes.add(registro);
            
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
                    imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarClientes.png"));
                    imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarClientes.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                    break; 
            default:
                if(tblClientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro",
                            "Eliminar Clientes", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarClientes(?)}");
                            procedimiento.setInt(1, ((Clientes)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                            listarClientes.remove(tblClientes.getSelectionModel().getSelectedItem());
                            
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
                if (tblClientes.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                    imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                    txtCodigoC.setEditable(false);
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
                    imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarClientes.png"));
                    imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesClientes.png")); 
                    limpiarControles();
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                break; 
                    
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
            Clientes registro = (Clientes)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNitCliente(txtNit.getText());
            registro.setNombreCliente(txtNombreC.getText());
            registro.setApellidoCliente(txtApellidoC.getText());
            registro.setDireccionCliente(txtDireccionC.getText());
            registro.setTelefonoCliente(txtTelefonoC.getText());
            registro.setCorreoCliente(txtCorreoC.getText());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNitCliente());
            procedimiento.setString(3, registro.getNombreCliente());
            procedimiento.setString(4, registro.getApellidoCliente());
            procedimiento.setString(5, registro.getDireccionCliente());
            procedimiento.setString(6, registro.getTelefonoCliente());
            procedimiento.setString(7, registro.getCorreoCliente());
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
                imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarClientes.png"));
                imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesClientes.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    public void desactivarControles(){
        txtCodigoC.setEditable(false);
        txtNombreC.setEditable(false);
        txtApellidoC.setEditable(false);
        txtDireccionC.setEditable(false);
        txtCorreoC.setEditable(false);
        txtNit.setEditable(false);
        txtTelefonoC.setEditable(false);
        
    }
    
    public void activarControles(){
        txtCodigoC.setEditable(true);
        txtNombreC.setEditable(true);
        txtApellidoC.setEditable(true);
        txtDireccionC.setEditable(true);
        txtCorreoC.setEditable(true);
        txtNit.setEditable(true);
        txtTelefonoC.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCorreoC.clear();
        txtNombreC.clear();
        txtApellidoC.clear();
        txtDireccionC.clear();
        txtCorreoC.clear();
        txtNit.clear();
        txtTelefonoC.clear();
 
    }
                
     public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     
     @FXML
       public void regresar (ActionEvent event){
           if (event.getSource() == btnRegresar){
                escenarioPrincipal.menuPrincipalView();
           }
       }
    
}
