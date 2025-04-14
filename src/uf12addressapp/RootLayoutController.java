package uf12addressapp;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import uf12addressapp.bd.GestorContactes;
import uf12addressapp.models.Contact;

public class RootLayoutController implements Initializable {

    private UF12AddressApp address_app;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setAddressApp(UF12AddressApp address_app) {
        this.address_app = address_app;
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void openFile() {
        File arxiu = this.mostraDialeg("open");
        if (arxiu != null) {
            this.address_app.loadContactDataFromFile(arxiu);
        }
    }

    @FXML
    public void saveFile() {
        File arxiu = this.address_app.getContactFilePath();
        if (arxiu != null) {
            this.address_app.saveContactDataToFile(arxiu);
        } else {
            this.saveAsFile();
        }
    }

    @FXML
    public void saveAsFile() {
        File arxiu = mostraDialeg("save");
        if (arxiu != null) {
            if (!arxiu.getPath().endsWith(".txt")) {
                arxiu = new File(arxiu.getPath() + ".txt");
            }
            this.address_app.saveContactDataToFile(arxiu);
        }
    }

    @FXML
    public void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre mi");
        alert.setHeaderText("Infomació del Autor");
        alert.setContentText("Nom i Cognom: José Luis Rubio");

        alert.showAndWait();
    }

    private File mostraDialeg(String tipus) {
        File arxiu;

        FileChooser seleccionador = new FileChooser();
        FileChooser.ExtensionFilter extensio = new FileChooser.ExtensionFilter(
                "Archivos de texto", "*.txt");

        seleccionador.getExtensionFilters().add(extensio);
        if (tipus.equals("save")) {
            return arxiu = seleccionador.showSaveDialog(
                    address_app.getPrimaryStage());
        } else {
            return arxiu = seleccionador.showOpenDialog(
                    address_app.getPrimaryStage());
        }
    }

    @FXML
    private void actualitzarBD() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Contact contacte : address_app.getContactes()) {
            if (contacte.getId() == -1) {
                int id = GestorContactes.inserirContacte(contacte);
                contacte.setId(id);
                ids.add(id);
            } else {
                GestorContactes.actualitzarContacte(contacte);
                ids.add(contacte.getId());
            }
        }
        GestorContactes.eliminarContactes(ids);
    }

}
