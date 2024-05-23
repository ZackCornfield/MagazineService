package magazine_service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * Controller class for the main view in the magazine service application.
 * Handles the loading and saving of the magazine along with the management of tabs 
 */
public class MainController {
    @FXML
    private TabPane TabPane;

    private CreateTabController createTabController;
    private EditTabController editTabController;
    private ViewTabController viewTabController;

    private MagazineService magazineService;
    
    @FXML
    public void initialize() {
        loadCreateTab();
        loadEditTab();
        loadViewTab(); 
    }
    
    private void loadCreateTab() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/CreateTab.fxml"));
        try {
            AnchorPane createTabContent = loader.load();
            createTabController = loader.getController();

            Tab createTab = new Tab("Create");
            createTab.setContent(createTabContent);
            TabPane.getTabs().add(createTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadEditTab() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/EditTab.fxml"));
        try {
            AnchorPane createTabContent = loader.load();
            editTabController = loader.getController();

            Tab createTab = new Tab("Edit");
            createTab.setContent(createTabContent);
            TabPane.getTabs().add(createTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadViewTab() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("resources/ViewTab.fxml"));
        try {
            AnchorPane createTabContent = loader.load();
            viewTabController = loader.getController();

            Tab createTab = new Tab("View");
            createTab.setContent(createTabContent);
            TabPane.getTabs().add(createTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveMagazine(ActionEvent e) {
        magazineService = editTabController.getMagazineService(); 
        try {
            // Create a file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save MagazineService");
            fileChooser.setInitialFileName("magazineService.ser");

            // Set the initial directory to the project folder
            String projectFolder = System.getProperty("user.dir");
            fileChooser.setInitialDirectory(new File(projectFolder));

            File file = fileChooser.showSaveDialog(null);

            // Serialize the magazineService object to the selected file
            if (file != null) {
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(magazineService);
                objectOut.close();
                fileOut.close();
                System.out.println("MagazineService saved successfully.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadMagazine() {
        try {
            // Create a file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load MagazineService");

            // Set the initial directory to the project folder
            String projectFolder = System.getProperty("user.dir");
            fileChooser.setInitialDirectory(new File(projectFolder));

            File file = fileChooser.showOpenDialog(null);

            // Deserialize the MagazineService object from the selected file
            if (file != null) {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                magazineService = (MagazineService) objectIn.readObject();
                objectIn.close();
                fileIn.close();
                System.out.println("MagazineService loaded successfully.");

                editTabController.setMagazineService(magazineService);
                viewTabController.setMagazineService(magazineService); 
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
