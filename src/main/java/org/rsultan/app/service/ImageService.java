package org.rsultan.app.service;

import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.inject.Singleton;

@Singleton
public class ImageService {

  private static final String SCALING_FACTOR_EXCEPTION_MSG =
      "scalingFactor must be between strictly between 0 and 1";

  public BufferedImage read(Path path) throws IOException {
    return ImageIO.read(path.toAbsolutePath().toFile());
  }

  public void write(Path path, BufferedImage image) throws IOException {
    ImageIO.write(image, "png", path.toAbsolutePath().toFile());
  }

  public BufferedImage resizeImage(BufferedImage source, double scalingFactor) {
    if (scalingFactor <= 0 || scalingFactor >= 1) {
      throw new IllegalArgumentException(SCALING_FACTOR_EXCEPTION_MSG);
    }
    int width = (int) (source.getWidth() * scalingFactor);
    int height = (int) (source.getHeight() * scalingFactor);
    var target = source.getScaledInstance(width, height, SCALE_SMOOTH);
    var outputImage = new BufferedImage(width, height, TYPE_INT_ARGB);
    outputImage.getGraphics().drawImage(target, 0, 0, null);
    return outputImage;
  }

}
