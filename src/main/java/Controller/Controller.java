package Controller;

import app.Books;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Books, String> colAuthor;

    @FXML
    private TableColumn<Books, Integer> colID;

    @FXML
    private TableColumn<Books, Integer> colPage;

    @FXML
    private TableColumn<Books, String> colTitle;

    @FXML
    private TableColumn<Books, Integer> colYear;

    @FXML
    private TextField tfAuthor;

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfPage;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfYear;

    @FXML
    private TableView<Books> tvBooks;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteButton();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();
    }


    public Connection getConnection(){
        // connect db vai jdbc, so set up a Connection object
        Connection conn;
        try{
            // the url after '3306/' should be the database name: e.g library
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
            return conn;
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public ObservableList<Books> getBookList(){
        // ObservableList: A list that enables listeners to track changes when they occur
        // observableArrayList: a list with arraylist behaviour
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        // Since we select all from books table, would need to have table call books in our database
        // The SQL
        String query = "SELECT * FROM books";
        // A statement is needed to execute the query
        Statement st;
        // ResultSet - a table of data representing a database result set
        ResultSet rs;

        try {
            st = conn.createStatement();
            // .executeQuery() return ResultSet object
            rs = st.executeQuery(query);
            Books books;
            while(rs.next()){
                // Add data into our Books class books object
                books = new Books(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                        rs.getInt("year"), rs.getInt("page"));
                // add as arraylist
                bookList.add(books);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return bookList;
    }

    public void showBooks(){
        ObservableList<Books> list = getBookList();

        colID.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));
        colPage.setCellValueFactory(new PropertyValueFactory<Books, Integer>("page"));

        tvBooks.setItems(list);
    }

    private void insertRecord(){
        String query = "INSERT INTO books VALUES (" + tfID.getText() + ",'" + tfTitle.getText() + "','" + tfAuthor.getText() + "',"
                + tfYear.getText() + "," + tfPage.getText() + ")";
        executeQuery(query);
        showBooks();
    }
    private void updateRecord(){
        String query = "UPDATE  books SET title  = '" + tfTitle.getText() + "', author = '" + tfAuthor.getText() + "', year = " +
                tfYear.getText() + ", page = " + tfPage.getText() + " WHERE id = " + tfID.getText() + "";
        executeQuery(query);
        showBooks();
    }
    private void deleteButton(){
        String query = "DELETE FROM books WHERE id =" + tfID.getText() + "";
        executeQuery(query);
        showBooks();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleMouseAction(MouseEvent event){
        Books books = tvBooks.getSelectionModel().getSelectedItem();
        tfID.setText("" + books.getId());
        tfTitle.setText(books.getTitle());
        tfAuthor.setText(books.getAuthor());
        tfYear.setText("" + books.getYear());
        tfPage.setText("" + books.getPage());
    }
}
