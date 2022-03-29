package reconnaissancefacial;

public class Main {
    public static void main(String[] args) {
        Vector[] vects = new Vector[4];
        vects[0] = new Vector(new double[]{1, 2, 3});
        vects[1] = new Vector(new double[]{7, 1, 3});
        vects[2] = new Vector(new double[]{0, 2, 5});
        vects[3] = new Vector(new double[]{8, 8, 3});
        Matrix mat = new Matrix(vects);


        ACP acp = new ACP(mat);
        acp.compute();


        Vector[] vects2 = new Vector[4];
        for (int i = 0; i < 4; i++) {
            vects2[i] = acp.getMatrixEigenVector().projectVector(vects[i]);
        }
        System.out.println(new Matrix(vects2));
    }

}
