package reconnaissancefacial;

import org.apache.commons.math3.linear.EigenDecomposition;
import java.util.HashMap;
import java.util.stream.Stream;

public class ACP {
    private Matrix matrixVectorsImage;
    private Vector vectorMean;
    private HashMap<Double, Vector> eigenVectors;

    public HashMap<Double, Vector> getEigenVectors() {
        return eigenVectors;
    }

    public ACP(Matrix matrixImage) {
        this.matrixVectorsImage = matrixImage;
        this.eigenVectors = new HashMap<>();
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
    public void compute() {

        // calcule du vecteur moyen
        this.vectorMean = this.matrixVectorsImage.computeMean();

        // centrer les vecteurs
        this.matrixVectorsImage.subtractAll(this.vectorMean);

        for (Vector vector : this.matrixVectorsImage.getVectors()) {
            vector.normalise();
        }

        // calcule des valeurs propres

        // necessite de transformer la matrice de cov en 'Array2DRowRealMatrix' pour utiliser la classe EigenDecomposition
        Matrix matrixA = this.getMatrixVectorsImage().transpose().multiply(this.getMatrixVectorsImage());
        EigenDecomposition eigenDecomposition = new EigenDecomposition(matrixA.toArray2DRowRealMatrix());

        // on stock les valeurs et vecteurs dans une map
        for (int i = 0; i < eigenDecomposition.getRealEigenvalues().length; i++) {
            double value = eigenDecomposition.getRealEigenvalue(i);

            if (Math.abs(value) > 1) {
                System.out.println(value);
                Vector vector = this.matrixVectorsImage.multiply(new Vector(eigenDecomposition.getEigenvector(i).toArray()).toMatrix(1)).toVector();
                vector.normalise();
                this.getEigenVectors().put(value, vector);
            }
        }
    }
}
