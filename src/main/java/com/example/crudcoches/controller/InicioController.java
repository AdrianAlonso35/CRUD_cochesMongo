package com.example.crudcoches.controller;

import com.example.crudcoches.CRUD.CocheCRUD;
import com.example.crudcoches.clases.Coche;
import com.example.crudcoches.utils.Alerta;

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

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class InicioController implements Initializable {


    @FXML
    private Button bttnEditar;

    @FXML
    private Button bttnEliminar;

    @FXML
    private Button bttnInsertar;

    @FXML
    private Button bttnLimpiar;

    @FXML
    private ComboBox<String> cbTipo;

    @FXML
    private TableView<Coche> tableView;

    @FXML
    private TableColumn<?, ?> tcMarca;

    @FXML
    private TableColumn<?, ?> tcMatricula;

    @FXML
    private TableColumn<?, ?> tcModelo;

    @FXML
    private TableColumn<?, ?> tcTipo;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtModelo;

    CocheCRUD cocheCRUD;

    ObservableList<Coche> listaCochesObservable;

    Coche cocheSeleccionado;



    public void cargarTabla(){
        ArrayList<Coche> listaCoches= cocheCRUD.getCoches();
        tableView.getItems().setAll(listaCoches);
    }
    @FXML
    void onInsertarClick(ActionEvent event) {
        String matricula = txtMatricula.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String tipo= cbTipo.getSelectionModel().getSelectedItem();

        if (matricula.isEmpty()|| modelo.isEmpty()||marca.isEmpty()||tipo.isEmpty()) {
            Alerta.alertaError("Rellena todos los campos");
        }else{
            if (cocheCRUD.comprobarMatricula(matricula)){
                Alerta.alertaError("El matricula ya existe");
            }else{
                Coche coche = new Coche(matricula,marca,modelo,tipo);
                if(cocheCRUD.insertarCoche(coche)==1){
                    Alerta.alertaInfo("Insertado correctamente");
                }else {
                    Alerta.alertaError("No se pudo insertar el coche");
                }
            }

        }
        cargarTabla();
    }
    @FXML
    void onEditarClick(ActionEvent event) {
        Coche coche= cocheSeleccionado;
        String matricula = txtMatricula.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String tipo= cbTipo.getSelectionModel().getSelectedItem();

        if (matricula.isEmpty()|| modelo.isEmpty()||marca.isEmpty()||tipo.isEmpty()) {
            Alerta.alertaError("Rellena todos los campos para poder editar el coche");
        }else {
            if (!Objects.equals(coche.getMatricula(), txtMatricula.getText())){
                Alerta.alertaError("No puedes editar la matricula");
            } else if (!Objects.equals(coche.getModelo(), txtModelo.getText())) {
                coche.setModelo(txtModelo.getText());
            } else if (!Objects.equals(coche.getTipo(), cbTipo.getSelectionModel().getSelectedItem())) {
                coche.setTipo(cbTipo.getSelectionModel().getSelectedItem());
            } else if (!Objects.equals(coche.getMarca(), txtMarca.getText())) {
                coche.setMarca(txtMarca.getText());
            }
            cocheCRUD.editarCoche(coche);
        }
        cargarTabla();
        limpiar();
    }

    @FXML
    void onEliminarClick(ActionEvent event) {
        System.out.println(cocheSeleccionado);
        cocheCRUD.eliminarCoche(cocheSeleccionado);
        cargarTabla();
        limpiar();
    }



    @FXML
    void onLimpiarClick(ActionEvent event) {
        limpiar();
    }
    public void limpiar(){
        txtMarca.clear();
        txtModelo.clear();
        txtMatricula.clear();
        cbTipo.getSelectionModel().clearSelection();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cocheCRUD = new CocheCRUD();
        cocheCRUD.crearBaseDatos();

        String[]lista= {"Todoterreno","Familiar","Suv","Deportivo"};
        cbTipo.getItems().addAll(lista);

        tcMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tcMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tcModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        ArrayList<Coche> listaCoches= cocheCRUD.getCoches();
        listaCochesObservable = FXCollections.observableArrayList(listaCoches);
        tableView.setItems(listaCochesObservable);

    }

    public void onMouseClick(javafx.scene.input.MouseEvent mouseEvent) {
        Coche coche = tableView.getSelectionModel().getSelectedItem();
        if (coche != null) {
            cocheSeleccionado = coche;
            txtMarca.setText(coche.getMarca());
            txtMatricula.setText(coche.getMatricula());
            txtModelo.setText(coche.getModelo());
            cbTipo.getSelectionModel().select(coche.getTipo());
        }
    }
}
