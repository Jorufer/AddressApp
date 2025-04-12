package uf12addressapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import uf12addressapp.models.Contact;
import java.io.IOException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UF12AddressApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Contact> contactes
            = FXCollections.observableArrayList();

    public UF12AddressApp() {

        this.contactes.add(new Contact("Guillermo", "Garrido Portes",
                "C/Albacete", "Valencia", 47001, 11, 01, 1995));

        this.contactes.add(new Contact("María", "Gómez Gil",
                "C/Alzira", "Alacant", 47002, 21, 02, 2000));

        this.contactes.add(new Contact("Diego", "Gonzalez Cuenca",
                "C/Manises", "Castello", 47003, 31, 03, 2005));

        this.contactes.add(new Contact("Laura", "Galiana Gutiérrez",
                "C/Xativa", "Barcelona", 47004, 01, 04, 2010));

        this.contactes.add(new Contact("Silvia", "Gandía García",
                "Plaza la Reina", "Valencia", 47005, 12, 05, 2015));

    }

    public ObservableList<Contact> getContactes() {
        return this.contactes;
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Assignem el primaryStage al stage inicial
        this.primaryStage = stage;
        //Canviem el titol
        this.primaryStage.setTitle("Activitat Avaluable 2 - Jose Luis Rubio");

        Image icona = new Image("resources/images/contacts.png");
        this.primaryStage.getIcons().add(icona);

        //La funcio initRootLayout inicialitza le Scene principal
        initRootLayout();

        //La funcio showIndex inicialitza la Scene interna
        showIndex();
    }

    private void initRootLayout() {
        try {
            //Carreguem el FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("views/RootLayout.fxml"));
            this.rootLayout = loader.load();
            //Creem una Scene amb l'arxiu FXML
            Scene scene = new Scene(this.rootLayout);
            //Assignem la Scene al Stage
            this.primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setAddressApp(this);
            //Mostrem el Stage
            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showIndex() {
        try {
            //Carreguem el FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("views/Index.fxml"));
            AnchorPane index = (AnchorPane) loader.load();
            this.rootLayout.setCenter(index);

            IndexController controller = loader.getController();
            controller.setAddressApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setContactFilePath(File arxiu) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        if (arxiu != null) {
            prefs.put("ruta_arxiu", arxiu.getPath());
        } else {
            prefs.remove("ruta_arxiu");
        }
    }

    public File getContactFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String ruta_arxiu = prefs.get("ruta_arxiu", null);
        if (ruta_arxiu != null) {
            return new File(ruta_arxiu);
        } else {
            return null;
        }
    }

    public Window getPrimaryStage() {
        return this.primaryStage;
    }

    public void saveContactDataToFile(File arxiu) {
        try {
            FileWriter fitxer = new FileWriter(arxiu);
            fitxer.write("");
            fitxer.close();
            fitxer = new FileWriter(arxiu, true);
            for (Contact contact : this.contactes) {
                String str = contact.getNom().get() + ","
                        + contact.getCognoms().get() + ","
                        + contact.getDomicili().get() + ","
                        + contact.getCiutat().get() + ","
                        + String.valueOf(contact.getCodi_postal().get()) + ","
                        + DateUtil.format(contact.getData_de_naixement().get());
                fitxer.write(str);
                fitxer.write(System.lineSeparator());
            }
            fitxer.close();
            this.setContactFilePath(arxiu);
        } catch (Exception ex) {
            System.err.println("Error al guardar els contactes en l'arxiu: "
                    + arxiu.getName());
        }
    }

    public void loadContactDataFromFile(File arxiu) {
        this.contactes.clear();
        try {
            FileReader fr = new FileReader(arxiu);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] contacte = linea.split(",");
                String[] fecha = contacte[5].split("\\.");
                this.contactes.add(new Contact(
                        contacte[0],
                        contacte[1],
                        contacte[2],
                        contacte[3],
                        Integer.parseInt(contacte[4]),
                        Integer.parseInt(fecha[0]),
                        Integer.parseInt(fecha[1]),
                        Integer.parseInt(fecha[2])));
            }
            fr.close();
            this.setContactFilePath(arxiu);
        } catch (Exception ex) {
            System.err.println("No s'ha trobat l'arxiu: " + arxiu.getName());
        }
    }

    //3.4.1.
    public boolean showContactEditDialog(Contact contacte) {
        try {
            //3.4.2
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/ContactEditDialog.fxml"));
            AnchorPane page = loader.load();

            //3.4.3.
            Stage dialogStage = new Stage();
            //3.4.4.
            dialogStage.setTitle(contacte.getNom().equals("") ? "Nou contacte" : "Editar "
                    + contacte.getNom() + " " + contacte.getCognoms());
            //3.4.5.
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            //3.4.6.
            Scene scene = new Scene(page);
            //3.4.7.
            dialogStage.setScene(scene);

            //3.4.8.
            ContactEditDialogController controller = loader.getController();
            //3.4.9.
            controller.setDialogStage(dialogStage);
            //3.4.10.
            controller.loadContacte(contacte);

            //3.4.11.
            dialogStage.showAndWait();

            //3.4.12.
            return controller.getAcceptClicked();
        } catch (IOException e) {
            e.printStackTrace();
            //3.4.13.
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
