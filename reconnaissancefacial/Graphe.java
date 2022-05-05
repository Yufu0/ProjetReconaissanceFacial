package reconnaissancefacial;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.HashMap;

public class Graphe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MySQL mysql = MySQL.getInstance();

        HashMap<Integer, String> images = mysql.getImages();

        int i = 0;
        Vector[] vectorsImages = new Vector[images.size()];
        for (int id : images.keySet()) {
            vectorsImages[i] = (new ImageProcessing(images.get(id), "N&B")).toMatrix().toVector();
            i++;
        }

        Matrix matrixImages = new Matrix(vectorsImages);

        ACP acp = new ACP(matrixImages, matrixImages.getWidth()-1);

        Matrix imageOrigine = (new ImageProcessing("img/Base_Images_Test/CELIO_BUERI_TEST/CELIO_BUERI_TEST_4.jpg", "N&B")).toMatrix();

        Matrix imageReconstruite = new Matrix(Init.WIDTH, Init.HEIGHT);
        imageReconstruite = imageReconstruite.add(acp.getVectorMean().toMatrix(Init.WIDTH));
        Vector tmp = imageOrigine.toVector();
        tmp.subtract(acp.getVectorMean());
        Vector projection = acp.getEigenMatrix().projectVector(tmp);

        XYChart.Series series = new XYChart.Series();
        series.setName("Evolution de la somme cummulée des valeurs propres");

        NumberAxis xAxis = new NumberAxis(0.0, matrixImages.getWidth()-1, 1);
        xAxis.setLabel("K");

        NumberAxis yAxis = new NumberAxis(0.0, 100000, 1000);
        yAxis.setLabel("Somme");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis,yAxis);


        double sommeEV = 0.0;
        double cummulEV = 0;

        for (int k = 0; k < acp.getEigenVectors().size(); k++) {
            sommeEV += (double) acp.getEigenVectors().keySet().toArray()[k];
        }

        series.getData().add(new XYChart.Data<>(0, imageOrigine.compareTo(imageReconstruite)));
        for (int k = 0; k < acp.getEigenVectors().size(); k++) {
            cummulEV += (double) acp.getEigenVectors().keySet().toArray()[k]/sommeEV;
            imageReconstruite = imageReconstruite.add(acp.getEigenMatrix().getVectors()[k].toMatrix(Init.WIDTH).multiplyByConstant(projection.get(k)));
            series.getData().add(new XYChart.Data<>(k+1, imageOrigine.compareTo(imageReconstruite)));
        }

        lineChart.getData().add(series);

        StackPane root = new StackPane();

        root.getChildren().add(lineChart);

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main (String[] args) {
        reconnaissancefacial.MySQL.getInstance().connexion();
        launch (args);
        reconnaissancefacial.MySQL.getInstance().deconnexion();
    }
}
