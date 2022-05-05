package reconnaissancefacial;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.io.Serializable;

public class Matrix implements Serializable {
    Vector[] data;

    public Matrix(Vector[] data) {
        this.data = data;
    }

    public Matrix(int width, int height) {
        this.data = new Vector[width];
        for (int x = 0; x < width; x++) {
            this.data[x] = new Vector(height);
            for (int y = 0; y < height; y++) {
                this.data[x].set(y, 0.0);
            }
        }
    }

    public int getWidth() {
        return this.data.length;
    }

    public int getHeight() {
        return this.data[0].getLenght();
    }

    public double get(int x, int y) {
        return this.data[x].get(y);
    }

    private Vector getVector(int x) {
        return this.data[x];
    }

    public void set(int x, int y, double val) {
        this.data[x].set(y, val);
    }

    public Vector toVector() {
        Vector vector =  new Vector(this.getWidth() * this.getHeight());
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                vector.set(x * this.getHeight() + y, this.get(x,y));
            }
        }
        return vector;
    }

    /* calcule du vecteur image moyen */
    public Vector computeMean() {
        Vector vectorMean =  new Vector(this.getHeight());
        double sum;
        for (int y = 0; y < this.getHeight(); y++) {
            sum = 0;
            for (int x = 0; x < this.getWidth(); x++) {
                sum += this.get(x, y);
            }
            vectorMean.set(y, sum / this.getWidth());
        }
        return vectorMean;
    }

    public void subtractAll(Vector vector) {
        for (int x = 0; x < getWidth(); x++) {
            this.getVector(x).subtract(vector);
        }
    }

    /* calcule et retourne la transposé de la matrice */
    public Matrix transpose() {
        Matrix transposeMatrix = new Matrix(this.getHeight(), this.getWidth());
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                transposeMatrix.set(y, x, this.get(x,y));
            }
        }
        return transposeMatrix;
    }

    public Matrix multiply(Matrix matrix) {
        if (this.getWidth() != matrix.getHeight()) return null;

        Matrix productMatrix = new Matrix(matrix.getWidth(), this.getHeight());
        double sum;
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                sum = 0.0;
                for (int z = 0; z < this.getWidth() ; z++) {
                    sum += this.get(z,y) * matrix.get(x,z);
                }
                productMatrix.set(x, y, sum);
            }
        }
        return productMatrix;
    }

    public Matrix add(Matrix matrix) {
        if (this.getWidth() != matrix.getWidth() || this.getHeight() != matrix.getHeight()) return null;

        Matrix sumMatrix = new Matrix(this.getWidth(), this.getHeight());
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                sumMatrix.set(x, y, this.get(x,y) + matrix.get(x,y));
            }
        }
        return sumMatrix;
    }

    public Array2DRowRealMatrix toArray2DRowRealMatrix() {
        double[][] table = new double[this.getWidth()][this.getHeight()];
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                table[x][y] = this.get(x,y);
            }
        }
        return new Array2DRowRealMatrix(table);
    }

    public Vector projectVector(Vector vector) {
        Matrix vectorMatrix = vector.toMatrix(1);
        Matrix projection = vectorMatrix.transpose().multiply(this);
        return projection.toVector();
    }


    @Override
    public String toString() {
        String str = "[";
        for (int x = 0; x < this.getWidth(); x++) {
            str += this.getVector(x).toString();
            if (x < this.getWidth()-1) str += ",\n";
        }
        str += "]";
        return str;
    }
}
