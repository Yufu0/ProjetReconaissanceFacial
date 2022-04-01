package reconnaissancefacial;

import org.apache.commons.math3.linear.EigenDecomposition;

import java.util.Arrays;

public class ACP {
    private Matrix matrixVectorsImage;
    private Vector vectorMean;
    private Matrix covarianceMatrix;
    private double[] eigenValues;
    private Matrix matrixEigenVector;
//    private HashMap<Double, Vector> michel;


    public ACP(Matrix matrixImage) {
        this.matrixVectorsImage = matrixImage;
    }

    public Matrix getMatrixVectorsImage() {
        return matrixVectorsImage;
    }

    public Vector getVectorMean() {
        return vectorMean;
    }

    public Matrix getCovarianceMatrix() {
        return covarianceMatrix;
    }

    public double[] getEigenValues() {
        return eigenValues;
    }

    public Matrix getMatrixEigenVector() {
        return matrixEigenVector;
    }

    public void compute() {

        System.out.println("data : "/* + this.getMatrixVectorsImage()*/);
        // calcule du vecteur moyen
        this.vectorMean = this.matrixVectorsImage.computeMean();
        System.out.println("mean : " /*+ this.getVectorMean()*/);

        // centrer les vecteurs
        this.matrixVectorsImage.subtractAll(this.vectorMean);
        System.out.println("center : " /*+ this.matrixVectorsImage*/);

        // calcule de la matrice de covariance
        this.computeCovarianceMatrix();
        System.out.println("cov : " /*+ this.getCovarianceMatrix()*/);


        // calcule des valeurs propres

        // necessite de transformer la matrice de cov en 'Array2DRowRealMatrix' pour utiliser eigenDecomposition

        EigenDecomposition eigenDecomposition = new EigenDecomposition(this.covarianceMatrix.toArray2DRowRealMatrix());
        this.eigenValues = eigenDecomposition.getRealEigenvalues();
        System.out.println("eigenvalues oof j'ai mal " /* + Arrays.toString(this.eigenValues)*/);

        // calcule des vecteurs propres
        this.matrixEigenVector = new Matrix(eigenDecomposition.getV().getData());
        System.out.println("eigenfaces de t'es morts : " /* + this.getMatrixEigenVector()*/);

        double sum = 0.0;
        for (int i = 0; i < this.eigenValues.length; i++) {
            sum += eigenDecomposition.getRealEigenvalue(i);
        }

        System.out.println(sum);
    }

    private void computeEigenValues() {
        this.eigenValues = new double[this.covarianceMatrix.getWidth()];
        for (int i = 0; i < this.covarianceMatrix.getWidth(); i++) {
            this.eigenValues[i] = this.covarianceMatrix.get(i,i);
        }
    }


    private void computeCovarianceMatrix() {
        int nbVariables = this.matrixVectorsImage.getHeight();
        int nbObservation = this.matrixVectorsImage.getWidth();
        this.covarianceMatrix = new Matrix(nbVariables, nbVariables);
        double cov_ij;
        // pour chaque pairs de variables
        for (int i = 0; i < nbVariables; i++) {
            for (int j = 0; j < nbVariables; j++) {
                // sum (x_i*y_i)
                cov_ij = 0.0;
                for (int x = 0; x < nbObservation; x++) {
                    cov_ij +=  this.matrixVectorsImage.get(x,i) * this.matrixVectorsImage.get(x,j);
                }
                // division par effectif - 1
                this.covarianceMatrix.set(i,j,cov_ij / (nbObservation - 1));
            }
        }
    }
}
