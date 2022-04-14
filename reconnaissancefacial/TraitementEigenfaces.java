package reconnaissancefacial;

import java.io.IOException;

public class TraitementEigenfaces {

    public TraitementEigenfaces(String[] arrayImages) throws IOException {
        Vector[] images = new Vector[arrayImages.length];
        for (int i = 0; i < arrayImages.length; i++) {
            images[i] = (new ImageProcessing(arrayImages[i], "N&B")).toMatrix().toVector();
        }
        Matrix matrixImages = new Matrix(images);
        ACP acp = new ACP(matrixImages);

        Vector[] projectedVectors = new Vector[arrayImages.length];
        for (int i = 0; i < arrayImages.length; i++) {
            projectedVectors[i] = acp.getEigenMatrix().projectVector(images[i]);
        }
    }
}
