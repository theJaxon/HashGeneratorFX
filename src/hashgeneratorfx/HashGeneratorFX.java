package hashgeneratorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HashGeneratorFX extends Application
{
    
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("HashGenerator");
        stage.setMaxWidth(1100);
        stage.setMinWidth(1000);
        stage.getIcons().add(
   new Image(
      HashGeneratorFX.class.getResourceAsStream( "HashLogo.png" ))); 

        stage.show();
    }


    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
