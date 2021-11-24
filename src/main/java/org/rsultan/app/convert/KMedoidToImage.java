package org.rsultan.app.convert;

import static java.util.stream.IntStream.range;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.inject.Singleton;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.rsultan.app.domain.KMedoidsWrapper;

@Singleton
public class KMedoidToImage implements Converter<BufferedImage, KMedoidsWrapper> {

  @Override
  public BufferedImage convert(KMedoidsWrapper kMedoidsWrapper) {
    var kMedoids = kMedoidsWrapper.kMedoids();
    var testDataframe = kMedoidsWrapper.testDataframe();

    var width = kMedoidsWrapper.image().getWidth();
    var height = kMedoidsWrapper.image().getHeight();

    var predictedDf = kMedoids.predict(testDataframe);
    var outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    var predictions = predictedDf.<Long>get("K");
    var squaredCluster = Nd4j.create(predictions).reshape(height, width);

    range(0, width).parallel().unordered().forEach(x ->
        range(0, height).parallel().unordered().forEach(y -> {
          var rgba = kMedoids.getCentroids().getRow(squaredCluster.getLong(y, x));
          var color = getColor(rgba);
          outImage.setRGB(x, y, color.getRGB());
        })
    );
    return outImage;
  }

  @Override
  public KMedoidsWrapper revert(BufferedImage target) {
    throw new RuntimeException("Not implemented");
  }

  private Color getColor(INDArray rgba) {
    int red = rgba.getInt(0);
    int green = rgba.getInt(1);
    int blue = rgba.getInt(2);
    int opacity = rgba.columns() > 3 ? rgba.getInt(3) : 255;
    return new Color(red, green, blue, opacity);
  }
}
