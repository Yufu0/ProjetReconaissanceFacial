package reconnaissancefacial;



import javafx.application.Application;
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

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Reconnaissance extends Application {
    public void start(Stage stage) {
        stage.setTitle("Reconnaissance faciale");
        stage.setResizable(false);
        FlowPane p = new FlowPane();

        p.setPrefSize(1000, 700);

        Button ajouterImg = new Button("Ajouter une image");
        ajouterImg.setPrefSize(300, 100);
        ajouterImg.setOnAction(e -> stage.setScene(ajouter(stage)));
        ajouterImg.getStyleClass().add("button-choice");

        Button tester = new Button("Tester un visage");
        tester.setPrefSize(300, 100);
        tester.setOnAction(e -> stage.setScene(tester(stage)));
        tester.getStyleClass().add("button-choice");

        p.getChildren().add(ajouterImg);
        p.getChildren().add(tester);
        p.setHgap(100);
        p.setAlignment(Pos.CENTER);
        p.getStyleClass().add("body");

        Scene scene = new Scene(p);
        scene.getStylesheets().add("./css/style.css");

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public Scene ajouter(Stage stage) {


        ImageView img = new ImageView();

        TextField nom = new TextField();
        nom.setPromptText("Nom");

        TextField prenom = new TextField();
        prenom.setPromptText("Prénom");

        BorderPane p = new BorderPane();
        p.setPrefSize(1000, 700);
        p.setPadding(new Insets(10, 10, 10, 10));
        p.getStyleClass().add("body");

        FlowPane menu = new FlowPane(Orientation.VERTICAL);
        menu.setPadding(new Insets(10, 10, 10, 10));
        menu.setHgap(5);
        menu.setVgap(10);
        menu.getStyleClass().add("menu");

        img.setFitHeight(700);
        img.setFitWidth(800);

        TreeView<String> treeView = new TreeView<String>(arborescence());

        final File[] f = new File[1];

        Button select = new Button("Sélectionner une image");
        select.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            f[0] = fc.showOpenDialog(null);
            if (f[0] != null) {
                img.setImage(new Image(f[0].toURI().toString()));
            }
        });
        Button ajout = new Button("Ajouter l'image");
        ajout.setOnAction(e -> {
            String nomText = nom.getText();
            String prenomText = prenom.getText();
            if (!(nomText.equals("")) && !(prenomText.equals(""))) {
                try {
                    InitDataBase.addImageToDataBase(nomText, prenomText, f[0].toString());
                    ImageDatabaseComputation.compute();
                    //treeView = new TreeView<String>(arborescence());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                Alert err = new Alert(AlertType.INFORMATION);
                err.setTitle("Erreur");
                err.setHeaderText(null);
                err.setContentText("Veuillez renseigner l'identité du visage");
                err.showAndWait();
            }
        });
        Button retour = new Button("Retour à l'accueil");
        retour.setOnAction(e -> start(stage));

        menu.getChildren().add(nom);
        menu.getChildren().add(prenom);
        menu.getChildren().add(select);
        menu.getChildren().add(ajout);
        menu.getChildren().add(retour);

        StackPane imgChoisie = new StackPane();
        imgChoisie.getChildren().add(img);
        imgChoisie.getStyleClass().add("image-view");



        p.setCenter(imgChoisie);
        p.setRight(menu);
        p.setLeft(treeView);

        BorderPane.setMargin(imgChoisie, new Insets(0, 10, 0, 10));

        Scene ajouter = new Scene(p);
        ajouter.getStylesheets().add("./css/style.css");

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
        File[] dir = (new File("img/DataBaseImage")).listFiles();
        TreeItem<String> root = new TreeItem<>("Images");
        root = arboR(root, dir);
        return(root);
    }

    public Scene tester(Stage stage) {

        TextField nomRec = new TextField();
        nomRec.setPromptText("Nom trouvé");
        nomRec.setEditable(false);

        TextField prenomRec = new TextField();
        prenomRec.setPromptText("Prénom trouvé");
        prenomRec.setEditable(false);


        BorderPane p = new BorderPane();
        p.setPrefSize(1000, 700);
        p.setPadding(new Insets(10, 10, 10, 10));

        FlowPane menu = new FlowPane(Orientation.VERTICAL);
        menu.setPadding(new Insets(10, 10, 10, 10));
        menu.setHgap(5);
        menu.setVgap(10);
        menu.getStyleClass().add("menu");

        StackPane imgEntree = new StackPane();
        imgEntree.setPrefWidth(400);
        imgEntree.setMinWidth(300);
        imgEntree.getStyleClass().add("image-view");

        StackPane imgSortie = new StackPane();
        imgSortie.setPrefWidth(400);
        imgSortie.setMinWidth(300);
        imgSortie.getStyleClass().add("image-view");

        ImageView img1 = new ImageView();
        img1.setFitHeight(300);
        img1.setFitWidth(300);

        ImageView img2 = new ImageView();
        img2.setFitHeight(300);
        img2.setFitWidth(300);

        final File[] f = new File[1];

        Button select = new Button("Sélectionner un visage");
        select.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            f[0] = fc.showOpenDialog(null);
            if (f[0] != null) {
                img1.setImage(new Image(f[0].toURI().toString()));
            }
        });
        Button tester = new Button("Tester un visage");
        tester.setOnAction(e -> {
            if (f[0] != null) {
                try {
                    FaceRecognition faceRecognition = new FaceRecognition(f[0].toString());
                    img2.setImage(new Image(new File(faceRecognition.getClosestImage()).toURI().toString()));
                    nomRec.setText(faceRecognition.getLastNameIdentified());
                    prenomRec.setText(faceRecognition.getFirstNameIdentified());
                } catch (IOException | SQLException | ClassNotFoundException exception) {
                    exception.printStackTrace();
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
        p.getStyleClass().add("body");

        BorderPane.setMargin(imgSortie, new Insets(0, 10, 0, 10));
        Scene test = new Scene(p);
        test.getStylesheets().add("./css/style.css");
        return test;
    }

    public static void main(String[] args) {
        reconnaissancefacial.MySQL.getInstance().connexion();
        launch(args);
        reconnaissancefacial.MySQL.getInstance().deconnexion();
    }
}
