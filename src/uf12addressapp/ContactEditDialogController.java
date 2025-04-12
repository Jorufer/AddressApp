package uf12addressapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uf12addressapp.models.Contact;

public class ContactEditDialogController implements Initializable {

    //3.2.7.
    @FXML
    private TextField nomField;
    @FXML
    private TextField cognomsField;
    @FXML
    private TextField domiciliField;
    @FXML
    private TextField ciutatField;
    @FXML
    private TextField codiPostalField;
    @FXML
    private TextField dataNaixField;

    //3.2.8.
    private Stage dialogStage;
    private boolean acceptClicked = false;
    private Contact contacte;

    //3.2.9.
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    //3.2.10.
    public boolean getAcceptClicked() {
        return acceptClicked;
    }

    //3.2.11.
    public void loadContacte(Contact contacte) {
        this.contacte = contacte;

        this.nomField.setText(contacte.getNom().get());
        this.cognomsField.setText(contacte.getCognoms().get());
        this.domiciliField.setText(contacte.getDomicili().get());
        this.ciutatField.setText(contacte.getCiutat().get());
        this.codiPostalField.setText(Integer.toString(contacte.getCodi_postal().get()));
        this.dataNaixField.setText(DateUtil.format(contacte.getData_de_naixement().get()));
    }

    //3.2.12.
    @FXML
    private void cancel() {
        dialogStage.close();
    }

    @FXML
    private void ok() {
        if (areFormInputsValid()) {
            contacte.getNom().set(nomField.getText());
            contacte.getCognoms().set(cognomsField.getText());
            contacte.getDomicili().set(domiciliField.getText());
            contacte.getCiutat().set(ciutatField.getText());
            contacte.getCodi_postal().set(Integer.parseInt(codiPostalField.getText()));
            contacte.getData_de_naixement().set(DateUtil.parse(dataNaixField.getText()));

            acceptClicked = true;
            dialogStage.close();
        }
    }

    private boolean areFormInputsValid() {
        String missatge = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            missatge += "Nom no vàlid\n";
        }
        if (cognomsField.getText() == null || cognomsField.getText().trim().isEmpty()) {
            missatge += "Cognoms no vàlid\n";
        }
        if (domiciliField.getText() == null || domiciliField.getText().trim().isEmpty()) {
            missatge += "Domicili no vàlid\n";
        }
        if (ciutatField.getText() == null || ciutatField.getText().trim().isEmpty()) {
            missatge += "Ciutat no vàlida\n";
        }
        if (codiPostalField.getText() == null || !codiPostalField.getText().matches("\\d+")) {
            missatge += "Codi postal no vàlid\n";
        }
        if (dataNaixField.getText() == null || dataNaixField.getText().trim().isEmpty()) {
            missatge += "Data de naixement no vàlida\n";
        } else if (!DateUtil.validDate(dataNaixField.getText())) {
            missatge += "Format de data incorrecte. Ha de ser dd.MM.yyyy\n";
        }

        if (missatge.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Camps incorrectes");
            alert.setContentText(missatge);
            alert.showAndWait();
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
