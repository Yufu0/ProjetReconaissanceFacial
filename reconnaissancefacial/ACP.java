package reconnaissancefacial;

import org.apache.commons.math3.linear.EigenDecomposition;
import java.util.HashMap;
import java.util.stream.Stream;

public class ACP {
    private Matrix matrixVectorsImage;
    private Vector vectorMean;
    private HashMap<Double, Vector> eigenVectors;
    private double quantiteInformationMinimum;

    public HashMap<Double, Vector> getEigenVectors() {
        return eigenVectors;
    }

    public ACP(Matrix matrixImage, double quantiteInformationMinimum) {
        this.matrixVectorsImage = matrixImage;
        this.eigenVectors = new HashMap<>();
        this.quantiteInformationMinimum = quantiteInformationMinimum;
        this.compute();
    }

    public Matrix getEigenMatrix() {
        return new Matrix(Stream.of(this.getEigenVectors().values().toArray()).toArray(Vector[]::new));
    }

    public Matrix getMatrixVectorsImage() {
        return matrixVectorsImage;
    }

    public Vector getVectorMean() {
        return vectorMean;
    }
    private void compute() {

        // calcule du vecteur moyen
        this.vectorMean = this.matrixVectorsImage.computeMean();

        // centrer les vecteurs
        this.matrixVectorsImage.subtractAll(this.vectorMean);

        // calcule des valeurs propres
        // necessite de transformer la matrice de cov en 'Array2DRowRealMatrix' pour utiliser la classe EigenDecomposition
        // A = tM * M
        Matrix matrixA = this.getMatrixVectorsImage().transpose().multiply(this.getMatrixVectorsImage());
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrixA.toArray2DRowRealMatrix());

        // on calcule la somme des valeurs propre
        double sumEigenvalues = 0.0;
        for (double eigenvalue : eigenDecomposition.getRealEigenvalues()) {
            sumEigenvalues += eigenvalue;
        }

        // on stock les valeurs et vecteurs dans une map
        // on garde les k permier vecteurs propre tel qu'ils aient au moins X % de l'information
        double currentSumEigenvalues = 0.0;
        for (int i = 0; i < eigenDecomposition.getRealEigenvalues().length; i++) {
            double eigenvalue = eigenDecomposition.getRealEigenvalue(i);


            if (currentSumEigenvalues < this.quantiteInformationMinimum * sumEigenvalues) {
                Vector eigenvector = this.matrixVectorsImage.multiply(new Vector(eigenDecomposition.getEigenvector(i).toArray()).toMatrix(1)).toVector();
                eigenvector.normalise();
                this.getEigenVectors().put(eigenvalue, eigenvector);
            }
            currentSumEigenvalues += eigenvalue;
        }
    }
}