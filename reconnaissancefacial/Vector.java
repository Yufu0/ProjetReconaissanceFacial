package reconnaissancefacial;

public class Vector {
    double[] data;

    public Vector(double[] data) {
        this.data = data;
    }

    public Vector(int size) {
        this.data = new double[size];
    }

    public int getLenght() {
        return this.data.length;
    }

    public double get(int i) {
        return this.data[i];
    }

    public void set(int i, double val) {
        this.data[i] = val;
    }

    public void subtract(Vector vector) {
        for (int i = 0; i < getLenght(); i++) {
            this.set(i, this.get(i) - vector.get(i));
        }
    }


    public Matrix toMatrix(int width) {
        int height = this.getLenght() / width;
        Matrix matrix = new Matrix(width, this.getLenght() / width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix.set(x,y,this.get(x * height + y));
            }
        }
        return matrix;
    }

    @Override
    public String toString() {
        String str = "{";
        for (int i = 0; i < this.getLenght(); i++) {
            str += this.get(i);
            if (i < this.getLenght()-1) str += ", ";
        }
        str += "}";
        return str;
    }

    public void normalise() {
        double norm = this.getNorm();
        for (int i = 0; i < this.getLenght(); i++) {
            this.set(i, this.get(i)/norm);
        }
    }

    public double getNorm() {
        double norm = 0.0;
        for (int i = 0; i < this.getLenght(); i++) {
            norm += this.get(i)*this.get(i);
        }
        return Math.sqrt(norm);
    }

    public double compareTo(Vector vector) {
        double distance = 0.0;
        for (int i = 0; i < this.getLenght(); i++) {
                distance += Math.pow(this.get(i)-vector.get(i), 2);
        }
        return Math.sqrt(distance);
    }
}