package reconnaissancefacial;

import org.apache.commons.math3.linear.EigenDecomposition;

import java.util.HashMap;
import java.util.stream.Stream;

public class ACP {
    private final Matrix matrixVectorsImage;
    private Vector vectorMean;
    private final HashMap<Double, Vector> eigenVectors;
    private final int nombreEigenFaces;

    public HashMap<Double, Vector> getEigenVectors() {
        return eigenVectors;
    }

    public ACP(Matrix matrixImage, int nombreEigenFaces) {
        this.matrixVectorsImage = matrixImage;
        this.eigenVectors = new LinkedHashMap<>();
        this.nombreEigenFaces = nombreEigenFaces;
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
        

        // on stock les valeurs et vecteurs dans une map
        // on garde les k permier vecteurs propre
        for (int i = 0; i < eigenDecomposition.getRealEigenvalues().length; i++) {
            double eigenvalue = eigenDecomposition.getRealEigenvalue(i);


            if (i < this.nombreEigenFaces) {
                Vector eigenvector = this.matrixVectorsImage.multiply(new Vector(eigenDecomposition.getEigenvector(i).toArray()).toMatrix(1)).toVector();
                eigenvector.normalise();
                this.getEigenVectors().put(eigenvalue, eigenvector);
            }
        }
    }
}
