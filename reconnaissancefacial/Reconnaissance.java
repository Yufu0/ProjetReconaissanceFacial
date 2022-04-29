package reconnaissancefacial;



import java.io.File;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Reconnaissance extends Application {
    public void start(Stage stage) {
        stage.setTitle("Reconnaissance faciale");
        stage.setResizable(false);
        FlowPane p = new FlowPane();
        p.setPrefSize(1000, 700);
        Button ajouterImg = new Button("Ajouter une image");
        ajouterImg.setPrefSize(300, 100);
        ajouterImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setScene(ajouter(stage));
            }
        });
        Button tester = new Button("Tester un visage");
        tester.setPrefSize(300, 100);
        tester.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.setScene(tester(stage));
            }
        });
        p.getChildren().add(ajouterImg);
        p.getChildren().add(tester);
        p.setHgap(100);
        p.setAlignment(Pos.CENTER);
        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public Scene ajouter(Stage stage) {
        BorderPane p = new BorderPane();
        FlowPane menu = new FlowPane(Orientation.VERTICAL);
        FlowPane arborescence = new FlowPane(Orientation.VERTICAL);
        StackPane imgChoisie = new StackPane();
        ImageView img = new ImageView();
        TextField nom = new TextField();

        TreeView<String> treeView = new TreeView<String>(arborescence());
        arborescence.getChildren().add(treeView);
        nom.setPromptText("Nom");
        TextField prenom = new TextField();
        prenom.setPromptText("Prénom");

        p.setPrefSize(1000, 700);
        p.setPadding(new Insets(10, 10, 10, 10));
        menu.setPadding(new Insets(10, 10, 10, 10));
        menu.setHgap(5);
        menu.setVgap(10);
        menu.setStyle("-fx-border-color: blue");
        arborescence.setStyle("-fx-border-color: cyan");
        imgChoisie.setStyle("-fx-border-color: green");

        img.setFitHeight(700);
        img.setFitWidth(800);

        Button select = new Button("Sélectionner une image");
        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fc = new FileChooser();
                File f = fc.showOpenDialog(null);
                if (f != null) {
                    img.setImage(new Image(f.toURI().toString()));
                }
            }
        });
        Button ajout = new Button("Ajouter l'image");
        ajout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String n = nom.getText();
                String p = prenom.getText();
                if (!(n.equals("")) && !(p.equals(""))) {

                } else {
                    Alert err = new Alert(AlertType.INFORMATION);
                    err.setTitle("Erreur");
                    err.setHeaderText(null);
                    err.setContentText("Veuillez renseigner l'identité du visage");
                    err.showAndWait();
                }

            }
        });
        Button retour = new Button("Retour à l'accueil");
        retour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                start(stage);
            }
        });

        menu.getChildren().add(nom);
        menu.getChildren().add(prenom);
        menu.getChildren().add(select);
        menu.getChildren().add(ajout);
        menu.getChildren().add(retour);
        imgChoisie.getChildren().add(img);
        p.setCenter(imgChoisie);
        p.setRight(menu);
        p.setLeft(arborescence);
        BorderPane.setMargin(imgChoisie, new Insets(0, 10, 0, 10));
        Scene ajouter = new Scene(p);
        return ajouter;
    }
    public TreeItem arboR(TreeItem root, File[] dir){
        File[] fichier;
        for (File file : dir) {
            if (file.isDirectory()){
                fichier = file.listFiles();
                TreeItem<String> rootSA = new TreeItem<>(file.getName());
                root.getChildren().addAll(
                        arboR(rootSA, fichier)
                );
            } else if (file.isFile()){
                root.getChildren().add(new TreeItem<String>(file.getName()));
            }
        }
        return(root);
    }
    public TreeItem arborescence(){
        File[] dirApprentissage = (new File("img/Base_Images_Apprentissage")).listFiles();
        File[] dirTest = (new File("img/Base_Images_Test")).listFiles();

        TreeItem<String> root = new TreeItem<>("Image");
        TreeItem<String> rootApprentissage = new TreeItem<>(new File("img/Base_Images_Apprentissage").getName());
        TreeItem<String> rootTest = new TreeItem<>(new File("img/Base_Images_Test").getName());

        rootApprentissage=arboR(rootApprentissage,dirApprentissage);
        rootTest=arboR(rootTest,dirTest);

        root.getChildren().addAll(
                rootApprentissage,
                rootTest
        );

        return(root);
    }

    public Scene tester(Stage stage) {
        BorderPane p = new BorderPane();
        FlowPane menu = new FlowPane(Orientation.VERTICAL);
        StackPane imgEntree = new StackPane();
        StackPane imgSortie = new StackPane();
        ImageView img1 = new ImageView();
        ImageView img2 = new ImageView();
        TextField nomRec = new TextField();
        TextField prenomRec = new TextField();
        nomRec.setPromptText("Nom trouvé");
        prenomRec.setPromptText("Prénom trouvé");


        p.setPrefSize(1000, 700);
        p.setPadding(new Insets(10, 10, 10, 10));
        menu.setPadding(new Insets(10, 10, 10, 10));
        menu.setHgap(5);
        menu.setVgap(10);
        menu.setStyle("-fx-border-color: blue");
        imgEntree.setStyle("-fx-border-color: green");
        imgSortie.setStyle("-fx-border-color: green");
        img1.setFitHeight(300);
        img1.setFitWidth(300);
        img2.setFitHeight(300);
        img2.setFitWidth(300);
        nomRec.setEditable(false);
        prenomRec.setEditable(false);

        Button select = new Button("Sélectionner un visage");
        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fc = new FileChooser();
                File f = fc.showOpenDialog(null);
                if (f != null) {
                    img1.setImage(new Image(f.toURI().toString()));
                }

            }
        });
        Button tester = new Button("Tester un visage");
        tester.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });
        Button retour = new Button("Retour à l'accueil");
        retour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                start(stage);
            }
        });

        menu.getChildren().add(select);
        menu.getChildren().add(tester);
        menu.getChildren().add(nomRec);
        menu.getChildren().add(prenomRec);
        menu.getChildren().add(retour);
        imgEntree.getChildren().add(img1);
        imgSortie.getChildren().add(img2);
        p.setLeft(imgEntree);
        p.setCenter(imgSortie);
        p.setRight(menu);
        BorderPane.setMargin(imgSortie, new Insets(0, 10, 0, 10));
        Scene test = new Scene(p);
        return test;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
