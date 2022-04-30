package application;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class FxmlLoader {

	private Pane view;
	
	public Pane getPage(String filename)
	{
		try
		{
		URL urlfile = Main.class.getResource("/view/" + filename + ".fxml");
		if(urlfile == null)
		{
			throw new java.io.FileNotFoundException("FXML file not found");
		}
		
		new FXMLLoader();
		view = FXMLLoader.load(urlfile);
		}
		catch(Exception exe) {
			
		}
		
		return view;
	}
}
