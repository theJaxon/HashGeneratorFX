package hashgeneratorfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;

/**
 * @author jaxon
 */
public class FXMLDocumentController implements Initializable {
    
    //@FXML private JFXButton BrowseButton;
    @FXML private JFXButton CopyMD5Button;
    @FXML private JFXButton CopySHA1Button;
    @FXML private JFXButton CopySHA256Button;
    @FXML private JFXTextField SelectedFilePath;
    @FXML private JFXTextField MD5TextField;
    @FXML private JFXTextField SHA1TextField;
    @FXML private JFXTextField SHA256TextField;
    @FXML private JFXComboBox HashTypesComboBox;
    @FXML private JFXToggleButton UppercaseToggleButton;
    @FXML private JFXTextField OriginalHashText;
    
    ArrayList <String> HashTypes = new ArrayList <>();
    NotificationPane notificationPane = new NotificationPane();   
    Image check = new Image("/Check.png");
    Image wrong = new Image("/Wrong.png");

    String FilePath;
    String MD5Digest;
    String SHA1Digest;
    String SHA256Digest;
    public static String newLine = System.getProperty("line.separator");
    FileInputStream FIS = null;
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();

  @FXML  private void BrowseButtonClicked(MouseEvent event) throws FileNotFoundException, IOException 
    {
        //Resetting the Button values when BrowseButton is clicked
        CopyMD5Button.setText("Copy MD5");
        CopySHA1Button.setText("Copy SHA1");
        CopySHA256Button.setText("Copy SHA256");

        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Select the file you want to generate the hash for");
        File file = filechooser.showOpenDialog(null);
        FilePath = file.getAbsolutePath();
        SelectedFilePath.setText(FilePath);
        InputStream IS = new FileInputStream(FilePath);
        FIS = new FileInputStream(new File(FilePath));
        //Getting the MD5 Hash
        byte data[] = org.apache.commons.codec.digest.DigestUtils.md5(FIS);
        char md5Chars[] = Hex.encodeHex(data);
        //Converting to String
        MD5Digest = String.valueOf(md5Chars);
        MD5TextField.setText(MD5Digest);
        
        //Getting SHA1 Hash
        SHA1Digest = DigestUtils.sha1Hex(IS);
        SHA1TextField.setText(SHA1Digest);
        //Getting SHA256 Hash
        SHA256Digest = DigestUtils.sha256Hex(IS);
        SHA256TextField.setText(SHA256Digest);
  
    }
    
     @FXML private void SaveHashesFunction(MouseEvent event)
    {
        FileChooser filechooser = new FileChooser();
        filechooser.setInitialFileName("Hashes.txt");
        filechooser.setTitle("Select a location for saving your hashes");
        File file; 
        filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT File(*.txt)", "*.txt"));
        PrintWriter HashesFile = null;
        
        if(MD5TextField.getText().trim().isEmpty())
        {
             Notifications.create().hideAfter(Duration.seconds(1.8)).title("No hashes").text("No Hashes to be saved.").showError();
        }
        else
        {
          file  = filechooser.showSaveDialog(null);
          
        try
        {
            HashesFile = new PrintWriter(file);
        }
        
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
          
          HashesFile.println("MD5:" + newLine  + MD5Digest 
          + newLine + newLine 
          + "SHA1:" + newLine + SHA1Digest 
          + newLine + newLine 
          + "SHA256:" + newLine + SHA256Digest);
          Notifications.create().hideAfter(Duration.seconds(3)).title("Success").text("File saved to " + file.getAbsolutePath() + ".").showInformation();
          HashesFile.close();
        }
    }
    
 @FXML private void UpperCaseFunction(ActionEvent event)
{
    if(UppercaseToggleButton.isSelected() == true)
    {
        MD5TextField.setText(MD5Digest.toUpperCase());
        SHA1TextField.setText(SHA1Digest.toUpperCase());
        SHA256TextField.setText(SHA256Digest.toUpperCase());
    }

    else 
    {
       MD5TextField.setText(MD5Digest.toLowerCase());
       SHA1TextField.setText(SHA1Digest.toLowerCase());
       SHA256TextField.setText(SHA256Digest.toLowerCase());
    }
}
    
    @FXML private void CopyMD5Function(ActionEvent event)
    {
        content.putString(MD5Digest);
        clipboard.setContent(content);
        CopyMD5Button.setText("Copied");
    }
    
    @FXML private void CopySHA1Function(ActionEvent event)
    {
        content.putString(SHA1Digest);
        clipboard.setContent(content);
        CopySHA1Button.setText("Copied");
    }
    
    @FXML private void CopySHA256Function(ActionEvent event)
    {
        content.putString(SHA256Digest);
        clipboard.setContent(content);
        CopySHA256Button.setText("Copied");
    }
    
    @FXML private void VerifyButtonFunction(MouseEvent event)
    {
        String OriginalValue = OriginalHashText.getText();
        String ComboBoxValue = HashTypesComboBox.getSelectionModel().getSelectedItem().toString();
        
        //Checking if MD5 is selected
        if(ComboBoxValue.equals("MD5"))
        {
            if(OriginalValue.equalsIgnoreCase(MD5Digest))
            {
                 Notifications.create().title("Success").text("MD5 Hash matches.").position(Pos.CENTER).graphic(new ImageView(check)).hideAfter(Duration.seconds(5)).show();
            }
            
            else 
            {
                Notifications.create().title("Error").text("MD5 Hash doesn't match.").position(Pos.CENTER).graphic(new ImageView(wrong)).hideAfter(Duration.seconds(5)).show();
            }
        }
        
        //If SHA1 is Selected
        if(ComboBoxValue.equals("SHA1"))
        {
            if(OriginalValue.equalsIgnoreCase(SHA1Digest))
            {
                Notifications.create().title("Success").text("SHA1 Hash matches.").position(Pos.CENTER).graphic(new ImageView(check)).hideAfter(Duration.seconds(5)).show();
            }
            else 
            {
                Notifications.create().title("Error").text("SHA1 Hash doesn't match.").position(Pos.CENTER).graphic(new ImageView(wrong)).hideAfter(Duration.seconds(5)).show();
            }
        }
        
        //If SHA256 is Selected
        if(ComboBoxValue.equals("SHA256"))
        {
            if(OriginalValue.equalsIgnoreCase(SHA256Digest))
            {
                Notifications.create().title("Success").text("SHA256 Hash matches.").position(Pos.CENTER).graphic(new ImageView(check)).hideAfter(Duration.seconds(5)).show();
            }
            else 
            {
                Notifications.create().title("Error").text("SHA256 Hash doesn't match.").position(Pos.CENTER).graphic(new ImageView(wrong)).hideAfter(Duration.seconds(5)).show();
            }
        }
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        HashTypes.add("MD5");
        HashTypes.add("SHA1");
        HashTypes.add("SHA256");
        HashTypesComboBox.getItems().addAll(HashTypes);
    }    
    
}
