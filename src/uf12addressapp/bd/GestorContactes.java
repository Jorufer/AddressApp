package uf12addressapp.bd;

import java.sql.*;
import java.util.ArrayList;
import uf12addressapp.models.Contact;

public class GestorContactes {

    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String user = "jorufer";
    private static final String password = "123456";

    private static final String queryInsert = "INSERT INTO contactes (nom, cognoms, domicili, ciutat, codi_postal, data_naix) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String queryUpdate = "UPDATE contactes SET nom = ?, cognoms = ?, domicili = ?, ciutat = ?, codi_postal = ?, data_naix = ? WHERE id = ?";
    private static final String queryDelete = "DELETE FROM contactes WHERE id = ?";

    private static Connection conn;
    private static PreparedStatement stmtInserir;
    private static PreparedStatement stmtActualitzar;
    private static PreparedStatement stmtEliminar;

    public static void connectarBD() {
        try {
            // Paso 1: conectar al servidor MySQL (sin base de dades)
            Connection connTemp = DriverManager.getConnection(url, user, password);
            Statement stmtTemp = connTemp.createStatement();

            // Paso 2: comprovar si existeix la BD
            ResultSet rsTemp = stmtTemp.executeQuery("SHOW DATABASES LIKE 'addressapp'");
            if (!rsTemp.next()) {
                // Paso 3: crear la base de dades i la taula
                stmtTemp.executeUpdate("CREATE DATABASE addressapp");
                stmtTemp.executeUpdate("CREATE TABLE addressapp.contactes ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT, "
                        + "nom VARCHAR(50) NOT NULL, "
                        + "cognoms VARCHAR(50) NOT NULL, "
                        + "domicili VARCHAR(100) NOT NULL, "
                        + "ciutat VARCHAR(50) NOT NULL, "
                        + "codi_postal INT NOT NULL, "
                        + "data_naix DATE NOT NULL"
                        + ")");
            }

            // Tancar connexió temporal
            rsTemp.close();
            stmtTemp.close();
            connTemp.close();

            // Paso 4: connectar-se a la BD addressapp
            conn = DriverManager.getConnection(url + "addressapp", user, password);

            // Paso 5: preparar els preparedStatements
            stmtInserir = conn.prepareStatement(
                    "INSERT INTO contactes (nom, cognoms, domicili, ciutat, codi_postal, data_naix) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmtActualitzar = conn.prepareStatement(
                    "UPDATE contactes SET nom = ?, cognoms = ?, domicili = ?, ciutat = ?, codi_postal = ?, data_naix = ? WHERE id = ?");

            stmtEliminar = conn.prepareStatement(
                    "DELETE FROM contactes WHERE id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Contact> carregarContactes() {
        ArrayList<Contact> contactes = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, nom, cognoms, domicili, ciutat, codi_postal, "
                    + "DAY(data_naix) AS dia, MONTH(data_naix) AS mes, YEAR(data_naix) AS any "
                    + "FROM contactes"
            );

            while (rs.next()) {
                Contact c = new Contact(
                        rs.getString("nom"),
                        rs.getString("cognoms"),
                        rs.getString("domicili"),
                        rs.getString("ciutat"),
                        rs.getInt("codi_postal"),
                        rs.getInt("dia"),
                        rs.getInt("mes"),
                        rs.getInt("any")
                );
                c.setId(rs.getInt("id"));
                contactes.add(c);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactes;
    }

    public static int inserirContacte(Contact contacte) {
        int idGenerat = -1;
        try {
            stmtInserir.setString(1, contacte.getNom().get());
            stmtInserir.setString(2, contacte.getCognoms().get());
            stmtInserir.setString(3, contacte.getDomicili().get());
            stmtInserir.setString(4, contacte.getCiutat().get());
            stmtInserir.setInt(5, contacte.getCodi_postal().get());
            stmtInserir.setDate(6, Date.valueOf(contacte.getData_de_naixement().get()));

            int filesInserides = stmtInserir.executeUpdate();

            if (filesInserides > 0) {
                try (ResultSet rs = stmtInserir.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerat = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerat;
    }

    public static void actualitzarContacte(Contact contacte) {
        try {
            stmtActualitzar.setString(1, contacte.getNom().get());
            stmtActualitzar.setString(2, contacte.getCognoms().get());
            stmtActualitzar.setString(3, contacte.getDomicili().get());
            stmtActualitzar.setString(4, contacte.getCiutat().get());
            stmtActualitzar.setInt(5, contacte.getCodi_postal().get());
            stmtActualitzar.setDate(6, Date.valueOf(contacte.getData_de_naixement().get()));
            stmtActualitzar.setInt(7, contacte.getId());

            stmtActualitzar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarContactes(ArrayList<Integer> idsAplicacio) {
        try {
            ArrayList<Integer> idsBD = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM contactes");

            while (rs.next()) {
                idsBD.add(rs.getInt("id"));
            }

            rs.close();
            stmt.close();

            // Restar listas
            idsBD.removeAll(idsAplicacio);

            for (int id : idsBD) {
                stmtEliminar.setInt(1, id);
                stmtEliminar.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void desconnectarBD() {
        try {
            if (stmtInserir != null) {
                stmtInserir.close();
            }
            if (stmtActualitzar != null) {
                stmtActualitzar.close();
            }
            if (stmtEliminar != null) {
                stmtEliminar.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
