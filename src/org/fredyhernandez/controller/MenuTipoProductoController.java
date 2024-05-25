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
import org.fredyhernandez.bean.TipoProducto;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuTipoProductoController implements Initializable {
     private Principal escenarioPrincipal;
     private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
     private operaciones tipoDeOperaciones   = operaciones.NINGUNO;
     private ObservableList<TipoProducto> listarTipoProducto;
     
     @FXML private Button btnRegresar;
     @FXML private TextField txtcodigoTP;
     @FXML private TextField txtdescripcion;
     @FXML private TableView tblTipoProducto;
     
     @FXML private TableColumn colcodigoTP;
     @FXML private TableColumn coldescripcion;
     
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
         tblTipoProducto.setItems(getTipoProducto());
         colcodigoTP.setCellValueFactory(new PropertyValueFactory<TipoProducto, Integer>("codigoTipoProducto"));
         coldescripcion.setCellValueFactory(new PropertyValueFactory<TipoProducto, String>("descripcion"));
         
    }
    
    public void seleccionarElemento(){
        txtcodigoTP.setText(String.valueOf(((TipoProducto) tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto()));
        txtdescripcion.setText(String.valueOf(((TipoProducto) tblTipoProducto.getSelectionModel().getSelectedItem()).getDescripcion()));
        
    }
    
    public ObservableList<TipoProducto> getTipoProducto(){
        ArrayList<TipoProducto> lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoProducto()}");
            ResultSet resultado = procedimiento.executeQuery();
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
                    imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarTipoProducto.png"));
                    imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarTipoProducto.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                    
        }
    }
    
    public void guardar(){
        TipoProducto registro = new TipoProducto();
        registro.setCodigoTipoProducto(Integer.parseInt(txtcodigoTP.getText()));
        registro.setDescripcion(txtdescripcion.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoProducto(?, ?)}");
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listarTipoProducto.add(registro);
            
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
                imagAgregar.setImage(new Image("/org/fredyhernandez/images/AgregarTipoProducto.png"));
                imagEliminar.setImage(new Image("/org/fredyhernandez/images/EliminarTipoProducto.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                if(tblTipoProducto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Confirmar si elimina el registro",
                            "Eliminar CargoEmpleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoProducto(?)}");
                            procedimiento.setInt(1, ((TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem()).getCodigoTipoProducto());
                            procedimiento.execute();
                            listarTipoProducto.remove(tblTipoProducto.getSelectionModel().getSelectedItem());
                            
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
                if (tblTipoProducto.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                    imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                    txtcodigoTP.setEditable(false);
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
                imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarTipoProducto.png"));
                imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesTipoProducto.png"));
                limpiarControles();
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
                
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoProducto(?, ?)}");
            TipoProducto registro = (TipoProducto)tblTipoProducto.getSelectionModel().getSelectedItem();
            registro.setCodigoTipoProducto(Integer.parseInt(txtcodigoTP.getText()));
            registro.setDescripcion(txtdescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoProducto());
            procedimiento.setString(2, registro.getDescripcion());
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
                imagEditar.setImage(new Image("/org/fredyhernandez/images/EditarTipoProducto.png"));
                imagReporte.setImage(new Image("/org/fredyhernandez/images/ReportesTipoProducto.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
        }
    }
    
    public void desactivarControles(){
        txtcodigoTP.setEditable(false);
        txtdescripcion.setEditable(false);
        
    }
    
    public void activarControles(){
        txtcodigoTP.setEditable(true);
        txtdescripcion.setEditable(true);
        
    }
    
    public void limpiarControles(){
        txtcodigoTP.clear();
        txtdescripcion.clear();
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
