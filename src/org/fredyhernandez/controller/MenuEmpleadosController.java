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
import org.fredyhernandez.bean.CargoEmpleados;
import org.fredyhernandez.bean.Empleados;
import org.fredyhernandez.db.Conexion;
import org.fredyhernandez.system.Principal;

public class MenuEmpleadosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {AGREGAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Empleados> listarEmpleados;
    private ObservableList<CargoEmpleados> listarCargoEmpleados;
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtSueldo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTurno;
    @FXML private TableView tblEmpleados;
    @FXML private Button btnRegresar;
    
    @FXML private ComboBox cmbCodigoCargoEmpleado;
    
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNombreEmpleado;
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colSueldo;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colCodigoCargoEmpleado;
    
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
        cmbCodigoCargoEmpleado.setItems(getCargoEmpleados());
        
    }    
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombreEmpleado"));
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, Double>("sueldo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccion"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Empleados, String>("turno"));
        colCodigoCargoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargoEmpleado"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNombreEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleado()));
        txtApellidoEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado()));
        txtSueldo.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
        txtDireccion.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccion()));
        txtTurno.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTurno()));
        cmbCodigoCargoEmpleado.getSelectionModel().select(buscarCargoEmpleado(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado()));
        
    }
    
    public CargoEmpleados  buscarCargoEmpleado(int codigoCargoEmpleado){
        CargoEmpleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarCargoEmpleados(?)}");
            procedimiento.setInt(1, codigoCargoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new CargoEmpleados(registro.getInt("codigoCargoEmpleado"),
                                                                           registro.getString("nombreCargo"),
                                                                           registro.getString("descripcionCargo")
                );
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            
        }
        
        return resultado;
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
    
    public ObservableList<CargoEmpleados> getCargoEmpleados(){
        ArrayList<CargoEmpleados> lista = new ArrayList<>();
        try{
            PreparedStatement procedimientos = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargoEmpleados()}");
            ResultSet resultado = procedimientos.executeQuery();
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
         Empleados registro = new Empleados();
          registro.setCodigoEmpleado(Integer.parseInt(txtCodigoEmpleado.getText()));
          registro.setNombreEmpleado(txtNombreEmpleado.getText());
          registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
          registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
          registro.setDireccion(txtDireccion.getText());
          registro.setTurno(txtTurno.getText());
          registro.setCodigoCargoEmpleado(((CargoEmpleados)cmbCodigoCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
          try{
              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?, ?, ?, ?, ?, ?, ?)}");
              procedimiento.setInt(1, registro.getCodigoEmpleado());
              procedimiento.setString(2, registro.getNombreEmpleado());
              procedimiento.setString(3, registro.getApellidoEmpleado());
              procedimiento.setDouble(4, registro.getSueldo());
              procedimiento.setString(5, registro.getDireccion());
              procedimiento.setString(6, registro.getTurno());
              procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
              procedimiento.execute();
              listarEmpleados.add(registro);
              
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
                 if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, "Confirmacion de eliminacion", 
                             "Eliminar Empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if(respuesta == JOptionPane.YES_NO_OPTION){
                         try{
                             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                             procedimiento.setInt(1, ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                             procedimiento.execute();
                             listarEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                             
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
                 if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                     activarControles();
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnAgregar.setDisable(true);
                     btnEliminar.setDisable(true);
                     imagEditar.setImage(new Image("/org/fredyhernandez/images/Editar.png"));
                     imagReporte.setImage(new Image("/org/fredyhernandez/images/Cancelar.png"));
                     txtCodigoEmpleado.setEditable(false);
                     tipoDeOperaciones = operaciones.ACTUALIZAR;
                     
                 }else
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento para editar");
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
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEmpleados(?, ?, ?, ?, ?, ?, ?)}");
             Empleados registro = (Empleados)tblEmpleados.getSelectionModel().getSelectedItem();
             registro.setNombreEmpleado(txtNombreEmpleado.getText());
             registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
             registro.setSueldo(Double.parseDouble(txtSueldo.getText()));
             registro.setDireccion(txtDireccion.getText());
             registro.setTurno(txtTurno.getText());
             registro.setCodigoCargoEmpleado(((CargoEmpleados)cmbCodigoCargoEmpleado.getSelectionModel().getSelectedItem()).getCodigoCargoEmpleado());
             procedimiento.setInt(1, registro.getCodigoEmpleado());
             procedimiento.setString(2, registro.getNombreEmpleado());
             procedimiento.setString(3, registro.getApellidoEmpleado());
             procedimiento.setDouble(4, registro.getSueldo());
             procedimiento.setString(5, registro.getDireccion());
             procedimiento.setString(6, registro.getTurno());
             procedimiento.setInt(7, registro.getCodigoCargoEmpleado());
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
       txtCodigoEmpleado.setEditable(false);
       txtNombreEmpleado.setEditable(false);
       txtApellidoEmpleado.setEditable(false);
       txtSueldo.setEditable(false);
       txtDireccion.setEditable(false);
       txtTurno.setEditable(false);
       cmbCodigoCargoEmpleado.setDisable(true);
       
    }
    
    public void activarControles(){
       txtCodigoEmpleado.setEditable(true);
       txtNombreEmpleado.setEditable(true);
       txtApellidoEmpleado.setEditable(true);
       txtSueldo.setEditable(true);
       txtDireccion.setEditable(true);
       txtTurno.setEditable(true);
       cmbCodigoCargoEmpleado.setDisable(false);
       
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtSueldo.clear();
        txtDireccion.clear();
        txtTurno.clear();
        cmbCodigoCargoEmpleado.getSelectionModel().getSelectedItem();
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
