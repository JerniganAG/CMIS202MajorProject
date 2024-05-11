// **********************************************
// Title:  Library Of Reigh
// Author: Alengta Jernigan
// Course Section: CMIS202-ONL1 (Seidel) Spring 2024
// File: AddBookEntry.java
// Description: UI for inputing book information
//Small collection of books sort by author's last name classify genere or number of pages
// **********************************************
package Lproject;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import javafx.beans.Observable;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Description Class
public class AddBookEntry {
	
	private static DisplayBL<String> outline = new DisplayBL<String>(); //static reference to Display.java
	private static BorderPane bPane = new BorderPane(); //reference to bPane for viewlibraryofreigh
	private static String bookPath = "src\\libraryofreigh.txt";//reference to file path
	
	//build the add book scene
	public static Scene buildBookScene() throws FileNotFoundException {
		//create panes
		//place nodes
		bPane.setTop(outline.DisplaySimpleHBox("Enter Book Information"));
		bPane.setCenter(BookInfo());
		bPane.setBottom(eventBox());
		
		//set scene
		Scene bookScene = new Scene(bPane);
		return bookScene;
	}
	
	//create the textfields, buttons, boxes
	private static HBox BookInfo() {
		//create panes
		VBox authorBox = new VBox(15);
		VBox miscBox = new VBox(15);
		
		//declaring textfields
		TextField titleText = new TextField();//title
		TextField fName = new TextField();//first name
		TextField mName = new TextField();//middle initial/name
		TextField lName = new TextField();//last name
		TextField pgCount = new TextField();//page count
		
		
                //ListView
		ListView<String> genreBox = new ListView<>();
		genreBox.getItems().addAll("Fantasy", "Sci-Fi", "Mystery", "Romance", "Comedy", "Young Adult", 
		 "Academic", "History", "Religion", "Horror", "Graphic Novel", "Short Story","Science");
		genreBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                /*ObservableList <String> genre;
                genre = genreBox.getSelectionModel().getSelectedItems();
                */
		//set contents of authorPane
		authorBox.getChildren().addAll((outline.DisplaySmallLabel("Title of The Book:")), titleText, outline.DisplaySmallLabel("Author's First Name:"), fName, 
				outline.DisplaySmallLabel("Author's Middle Initial:"), mName, outline.DisplaySmallLabel("Author's Last Name:"), lName);
		
		//Set contents of miscPane
		miscBox.getChildren().addAll(outline.DisplaySmallLabel("Page Count:"), pgCount, outline.DisplaySmallLabel("Genre: "
                        + "Hit Ctrl button on keyboard to Select Multiple Genere "), genreBox);
		
		//Vbox
		HBox hBox = new HBox();
		hBox.getChildren().addAll(authorBox, miscBox);
		hBox.setAlignment(Pos.BOTTOM_CENTER);
		hBox.setPadding(new Insets(150, 50, 50, 50));
		
		return hBox;
	}
	
	//save the book to file
	@SuppressWarnings("unchecked")
	public static void saveBook(BorderPane bPane) {
		PrintWriter file;
		Book book = new Book();
		
		//record information
		try {
			//get contents of borderpane
			HBox centerBox = (HBox) bPane.getCenter();
			VBox leftBox = (VBox) centerBox.getChildren().get(0);
			VBox rightBox = (VBox) centerBox.getChildren().get(1);
			
			//record author
			String firstName = (String) ((TextField) leftBox.getChildren().get(3)).getText();
			String midInitial = (String) ((TextField) leftBox.getChildren().get(5)).getText();
			String lastName = (String) ((TextField) leftBox.getChildren().get(7)).getText();
			book.setAuthor(firstName, midInitial, lastName);		
			
			//record title
			book.setTitle((String) ((TextField) leftBox.getChildren().get(1)).getText());
			
			//record page count
			String pgCt = ((String) (((TextField) rightBox.getChildren().get(1)).getText()));
			book.setPageCount(Integer.parseInt(pgCt));
			
			//record genre
			book.setGenre((String) ((ListView<String>)rightBox.getChildren().get(3)).getSelectionModel().getSelectedItem());
			
			//Record book to file if not comic
			file = new PrintWriter(new FileOutputStream(new File(bookPath), true));
			file.println(book.recordBook());
			file.close();
			
		}catch (Exception e) {
			outline.DisplayErrorMessage(e.toString());
			
		}
	}
	
	//create buttons for ui
	private static HBox eventBox() throws FileNotFoundException {
		//set up hbox
		HBox hBox = new HBox(15);
		hBox.setPadding(new Insets(15, 15, 15, 15));
		hBox.setStyle("-fx-background-color: slategray");
		EventMain eventManager = new EventMain();
				
		//Back to main menu
		Button btHome = outline.DisplayButton("Home");
		btHome.setOnAction(eventManager.buttonHome);
				
		//View Library
		Button btView = outline.DisplayButton("Library");
		btView.setOnAction(eventManager.buttonView);
		
                
		//Add book, restarts the add book menu without saving
		Button btSave = outline.DisplayButton("Save Book");
		btSave.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent e) {
                saveBook(bPane);
            }
        });

		//Quit the application
		Button btQuit = outline.DisplayButton("Quit");
		btQuit.setOnAction(eventManager.buttonQuit);
				
		//add buttons
		hBox.getChildren().addAll(btHome, btView, btSave, btQuit);
				
		//set alignment
		hBox.setAlignment(Pos.CENTER);
				
		return hBox;
	}
}