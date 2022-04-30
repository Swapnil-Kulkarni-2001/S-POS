package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SettingsController implements Initializable{

	public String url="jdbc:h2:~/PosDB";
	public String username="sa";
	public String password="";
	public Connection conn;
	public String uimage;
	
	
	    @FXML
	    private Label labelusername;

	    @FXML
	    private Label labelshopname;

	    @FXML
	    private JFXTextField txtusername;

	    @FXML
	    private JFXPasswordField txtpassword;

	    @FXML
	    private JFXTextField txtshopname;

	    @FXML
	    private JFXTextField txtshoplogo;
	    
	    @FXML
	    private JFXTextField counter_number;
	    
	    @FXML
	    private JFXToggleButton togglebtn;
	    
	    @FXML 
	    private ImageView userimage;

	    @FXML
	    void btninfochangeclicked(MouseEvent event) {
	    	String pass="";
	    	try
	    	{
				Class.forName("org.h2.Driver");
				conn = DriverManager.getConnection(url,username,password);
				ResultSet rst = conn.createStatement().executeQuery("select user_password from info_table");
				if(rst.next())
				{
					pass=rst.getString("user_password");
					
				}
				else {
					pass="N/A";
				}
				conn.close();
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println(e);
	    		pass="N/A";
	    	}

		    PasswordDialog pd = new PasswordDialog();
		    Optional<String> result = pd.showAndWait();
		    //result.ifPresent(password -> System.out.println(password));
		    if(result.isPresent())
		    {
		    	if(result.get().equals(pass))
		    	{
		    		System.out.println("correct password!!!");
		    		txtusername.setEditable(true);
		    		txtpassword.setEditable(true);
		    		txtshopname.setEditable(true);
		    		counter_number.setEditable(true);
		    	}
		    	else {
		    		   System.out.println("Password incorrecrt!!");
			    	   Alert alert = new Alert(AlertType.ERROR);
			    	   alert.setTitle("StarPoS");
			    	   alert.setContentText("incorrect password!!");
			    	   alert.setHeaderText(null);
			    	   alert.show(); 
		    	}
		    }

	    }

	   

	   @FXML
	   public void btnchangeshopimage(MouseEvent me)
	   {
		   FileChooser fc = new FileChooser();
		   fc.getExtensionFilters().addAll(new ExtensionFilter("PNG Files","*.png"));
		   File selectedfile = fc.showOpenDialog(null);
		   
		   if(selectedfile!=null)
		   {
			   txtshoplogo.setText(selectedfile.getAbsolutePath());
		   }
		   else
		   {
			   System.out.println("file is not valid");
		   }
		   
	   }
	   
	   @FXML
	    void btnsaveclicked(ActionEvent event) {
	    	String pass="";
	    	String sec="";
	    	try
	    	{
				Class.forName("org.h2.Driver");
				conn = DriverManager.getConnection(url,username,password);
				ResultSet rst = conn.createStatement().executeQuery("select user_password from info_table");
				if(rst.next())
				{
					pass=rst.getString("user_password");
					
				}
				else {
					pass="N/A";
				}
				conn.close();
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.println(e);
	    		pass="N/A";
	    	}

		    PasswordDialog pd = new PasswordDialog();
		    Optional<String> result = pd.showAndWait();
		    //result.ifPresent(password -> System.out.println(password));
		    if(result.isPresent())
		    {
		    	if(result.get().equals(pass))
		    	{
		    		System.out.println("correct password!!!");
		    		if(txtusername.getText()!="" && isValidPassword(txtpassword.getText()) && txtshopname.getText()!="" && txtshoplogo.getText()!="")
		    		{
		    			try {
		    				String query="update info_table set user_name=?,user_password=?,shop_name=?,shop_logo=?,user_image=?,counter_no=?,secure=?";
							Class.forName("org.h2.Driver");
							conn = DriverManager.getConnection(url,username,password);
							PreparedStatement st = conn.prepareStatement(query);
							st.setString(1,txtusername.getText());
							st.setString(2, txtpassword.getText());
							st.setString(3, txtshopname.getText());
							st.setString(4, txtshoplogo.getText());
							st.setString(5, uimage);
							if(counter_number.getText().equals(""))
							{
								counter_number.setText("N/A");
							}
							st.setString(6, counter_number.getText());
							if(togglebtn.isSelected())
							{
								sec="Yes";
							}
							else
							{
								sec="No";
							}
							st.setString(7, sec);
							int r = st.executeUpdate();
				    		txtusername.setEditable(false);
				    		txtpassword.setEditable(false);
				    		txtshopname.setEditable(false);
				    		counter_number.setEditable(false);
							System.out.println(r+" rows affected");
							if(r>0)
							{
						    	   Alert alert = new Alert(AlertType.INFORMATION);
						    	   alert.setTitle("StarPoS");
						    	   alert.setContentText("Information changed!!");
						    	   alert.setHeaderText(null);
						    	   alert.show();
							}
			    		}catch(Exception e) {
			    			
			    		}
		    		}
		    		else {
				    	   Alert alert = new Alert(AlertType.ERROR);
				    	   alert.setTitle("StarPoS");
				    	   alert.setContentText("Make sure all fields are not empty.\nAnd your password must contain atleast 8 characters \nwith atleat 1 special symbol and \natleat 1 Capital letter");
				    	   alert.setHeaderText(null);
				    	   alert.show();
		    		}
		    		

					
		    		
		    	}
		    	else {
		    		   System.out.println("Password incorrecrt!!");
			    	   Alert alert = new Alert(AlertType.ERROR);
			    	   alert.setTitle("S-PoS");
			    	   alert.setContentText("incorrect password!!");
			    	   alert.setHeaderText(null);
			    	   alert.show(); 
		    	}
		    }

	    }
	   
	   @FXML
	   public void changeuserimage(MouseEvent me) throws FileNotFoundException
	   {
		   FileChooser fc = new FileChooser();
		   FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
		   fc.getExtensionFilters().add(imageFilter);
		   File selectedfile = fc.showOpenDialog(null);
		   
		   if(selectedfile!=null)
		   {
			   InputStream stream = new FileInputStream(selectedfile.getAbsolutePath());
			   userimage.setImage(new Image(stream));
			   uimage=selectedfile.getAbsolutePath();
		   }
		   else
		   {
			   System.out.println("file is not valid");
			   uimage = "./src/view/male_user_60px.png";
		   }
	   }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		String userName="N/A";
		String userpassword="N/A";
		String shopname="N/A";
		String shoplogo="N/A";
		String secure="N/A";
		String counterno="N/A";
		String uimage = "./src/view/male_user_60px.png";
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(url,username,password);
			ResultSet rst = conn.createStatement().executeQuery("select * from info_table");
			if(rst.next())
			{
				userName=rst.getString("user_name");
				userpassword=rst.getString("user_password");
				shopname=rst.getString("shop_name");
				shoplogo=rst.getString("shop_logo");
				uimage=rst.getString("user_image");
				counterno=rst.getString("counter_no");
				secure=rst.getString("secure");
			}
			if(uimage==null)
			{
				uimage = "./src/view/male_user_60px.png";
			}
			if(secure.equals("Yes"))
			{
				togglebtn.setSelected(true);
			}
			else
			{
				togglebtn.setSelected(false);
			}
			labelusername.setText(userName);
			labelshopname.setText(shopname);
			txtusername.setText(userName);
			txtpassword.setText(userpassword);
			txtshopname.setText(shopname);
			txtshoplogo.setText(shoplogo);
			counter_number.setText(counterno);
			InputStream stream = new FileInputStream(uimage);
			userimage.setImage(new Image(stream));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
	public boolean isValidPassword(String password)
    {
  
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                       + "(?=.*[a-z])(?=.*[A-Z])"
                       + "(?=.*[@#$%^&+=])"
                       + "(?=\\S+$).{8,20}$";
  
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
  
        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }
  
        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);
  
        // Return if the password
        // matched the ReGex
        return m.matches();
    }
	
}
