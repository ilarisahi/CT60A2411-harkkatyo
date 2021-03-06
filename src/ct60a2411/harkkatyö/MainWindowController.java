package ct60a2411.harkkatyö;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * 
 * @author Petri Rämö
 * opiskelijanro: 0438578
 * 
 * @author Ilari Sahi
 * opiskelijanro: 0438594
 * 
 * 16.12.2016
 */
public class MainWindowController implements Initializable {
    
    private ArrayList<SmartPost> SPList;
    private SmartPostContainer smartPosts = SmartPostContainer.getInstance();
    private Warehouse warehouse = Warehouse.getInstance();
    private LogWriter lw;
    
    @FXML
    private ComboBox<String> autoCombo;
    @FXML
    private Button addToMap;
    @FXML
    private Button createBut;
    @FXML
    private ComboBox<Parcel> packageCombo;
    @FXML
    private Button removeRoute;
    @FXML
    private Button sendBut;
    @FXML
    private WebView web;
    @FXML
    private Tab log;
    @FXML
    private TextArea logArea;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Tab smartpost;
    @FXML
    private Label sentParcelCounter;
    @FXML
    private Label distanceCounter;
    @FXML
    private Button endButton;
    
    /**
     * This class contains all all functions that first window uses. At initialize()
     * it initializes comboboxes, webview and log file.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        web.getEngine().load(getClass().getResource("index.html").toExternalForm());
        XMLReader xmlr = XMLReader.getInstance();
        autoCombo.getItems().addAll(smartPosts.getCities());
        autoCombo.getSelectionModel().selectFirst();
        sentParcelCounter.setText("0");
        distanceCounter.setText("0.0");        
        loadParcels();
        
        try {
            lw = LogWriter.getInstance();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    /**
     * This button prints all automatons of selected city.
     * 
     * @param event 
     */
    @FXML
    private void addToMapAction(ActionEvent event) {
        String name = autoCombo.getValue();
        for (SmartPost sPost : smartPosts.getCitySmartPosts(name)) {
            String open = "<p>" + sPost.getPostoffice() + "</p><p>Auki: " + sPost.getAvailability() + "</p>";
            web.getEngine().executeScript("document.goToLocation(" + sPost.getLat() + "," + sPost.getLng() + ",'" + open + "', 'blue')");
        }
    }
    
    /**
     * This button opens up a window where you can make a new package.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void createButAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateParcelWindow.fxml"));
        Parent root = loader.load();        
        Stage newStage = new Stage();
        
        // Sends couple of objects to new controller for easier update actions
        CreateParcelWindowController controller = loader.getController();
        controller.setWeb(web);
        controller.setParcelBox(packageCombo);
        controller.setSendBut(sendBut);
        
        root.getStylesheets().addAll(getClass().getResource("style.css").toExternalForm());
        newStage.setTitle("TIMO - luo paketti");
        newStage.getIcons().add(new Image(getClass().getResourceAsStream("assets/timo_icon.png")));
        newStage.getIcons().add(new Image(getClass().getResourceAsStream("assets/timo_icon_big.png")));
        
        // New window can't be resized and it will remain on top until closed
        newStage.setResizable(false);
        newStage.initModality(Modality.APPLICATION_MODAL);
        
        newStage.setScene(new Scene(root));
        newStage.showAndWait();
    }
    
    /**
     * This button all routes and automaton from map.
     * 
     * @param event 
     */
    @FXML
    private void removeRouteAction(ActionEvent event) {
        web.getEngine().executeScript("document.deletePaths()");
    }

    /**
     * This button sends the package that you have made and selected. When you do this
     * it puts beginning and end automaton to the maps and starts drawing route that it uses
     * to get to these points.
     * 
     * @param event
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    @FXML
    private void sendButAction(ActionEvent event) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        boolean broke;
        
        Parcel parcel = packageCombo.getValue();
        SmartPost startPost = SmartPostContainer.getInstance().getSmartPost(parcel.getStartPost());
        SmartPost endPost = SmartPostContainer.getInstance().getSmartPost(parcel.getEndPost());
        ArrayList<Double> array = new ArrayList();
        array.add(startPost.getLat());
        array.add(startPost.getLng());
        array.add(endPost.getLat());
        array.add(endPost.getLng());
        String stringDistance = String.valueOf(web.getEngine().executeScript("document.pathDist(" + array + ")"));
        String color = "red";
        
        // Insert end and start SmartPosts to map and draw route
        String open = "<p>" + startPost.getPostoffice() + "</p><p>Auki: " + startPost.getAvailability() + "</p>";
        web.getEngine().executeScript("document.goToLocation(" + startPost.getLat() + "," + startPost.getLng() + ",'" + open + "', 'blue')");
        open = "<p>" + endPost.getPostoffice() + "</p><p>Auki: " + endPost.getAvailability() + "</p>";
        web.getEngine().executeScript("document.goToLocation(" + endPost.getLat() + "," + endPost.getLng() + ",'" + open + "', 'blue')");
        
        web.getEngine().executeScript("document.createPath(" + array + ",'" + color + "'," + parcel.getGrade() + ",'" + parcel.getItem().getName() + "')");
        
        // Strings to write log
        String name = parcel.getItem().getName();
        String start = startPost.getAddress() + ", " + startPost.getCity();
        String end = endPost.getAddress() + ", " + endPost.getCity();
        
        // Check if item broke
        if (parcel.getItem().isFragile() && (parcel.getItem().getFragile_factor() < parcel.getFragile_factor())) {
            broke = true;
        } else {
            broke = false;
        }
        
        // Update log accordingly
        lw.writer(name, start, end, broke, stringDistance);
        sentParcelCounter.setText(String.valueOf(Integer.parseInt(sentParcelCounter.getText()) + 1));
        distanceCounter.setText(String.valueOf(Double.parseDouble(distanceCounter.getText()) + Double.parseDouble(stringDistance)));
        
        // Update parcel box and warehouse
        warehouse.deleteParcel(parcel);
        packageCombo.getSelectionModel().clearSelection();
        packageCombo.getItems().clear();
        loadParcels();
        
        
        // Update log area
        if (!logArea.getText().trim().isEmpty()){
            logArea.clear();
        }
            
        try {
            String loggerino = lw.reader();
            logArea.setText(loggerino);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    @FXML
    private void endButtonAction(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * Loads parcels in warehouse to the ComboBox (if any)
     */
    private void loadParcels() {
        if (Warehouse.getParcels().isEmpty()) {
            packageCombo.setDisable(true);
            sendBut.setDisable(true);
        } else {
            packageCombo.getItems().addAll(Warehouse.getParcels());
            packageCombo.getSelectionModel().selectFirst();
            
        }
    }

    /**
     * Writes closing information to log.txt
     * Writes parcels that were left in the warehouse
     */
    public void closeAction() {
        try {
            lw.endWrite(sentParcelCounter.getText(), distanceCounter.getText());
            lw.endInitWarehouse();
            
            // Loop for each parcel left in warehouse
            for (Parcel p : Warehouse.getParcels()) {
                SmartPost sP = smartPosts.getSmartPost(p.getStartPost());
                SmartPost eP = smartPosts.getSmartPost(p.getEndPost());
                
                ArrayList<Double> array = new ArrayList();
                array.add(sP.getLat());
                array.add(sP.getLng());
                array.add(eP.getLat());
                array.add(eP.getLng());
                String distance = String.valueOf(web.getEngine().executeScript("document.pathDist(" + array + ")"));
        
                lw.endWriteWarehouse(p.getItem().getName(), sP.getAddress() + ", " + sP.getCity(), eP.getAddress() + ", " + eP.getCity(), distance);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
