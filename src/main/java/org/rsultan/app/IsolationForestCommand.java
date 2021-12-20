package org.rsultan.app;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.rsultan.app.convert.ImageToDataframe;
import org.rsultan.app.convert.ImageToRGBDataframe;
import org.rsultan.app.convert.KMedoidToImage;
import org.rsultan.app.service.HeatMapFactory;
import org.rsultan.app.service.ImageService;
import org.rsultan.app.service.IsolationForestService;
import org.rsultan.app.service.IsolationForestService.IForestType;
import org.rsultan.dataframe.Dataframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.ToDoubleFunction;

@Command(name = "iforest", mixinStandardHelpOptions = true)
public class IsolationForestCommand extends IOFileCommand implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(IsolationForestCommand.class);

  private static final String N_DESC = "the number of trees to train your forest";
  private static final String T_DESC = "the type of isolation forest (REGULAR|EXTENDED)";
  private static final Color DEFAULT_COLOR = new Color(0, 255, 0, 255);

  @Option(names = {"-n", "--number-of-trees"}, description = N_DESC, required = true)
  int numberOfTrees;

  @Option(names = {"-t", "--type"}, description = T_DESC, required = true)
  IForestType forestType;


  @Inject
  private IsolationForestService isolationForestService;

  @Inject
  private ImageService imageService;

  @Inject
  private ImageToRGBDataframe imageToDataframe;

  @Inject
  private HeatMapFactory heatMapFactory;

  public static void main(String[] args) {
    PicocliRunner.run(IsolationForestCommand.class, args);
  }

  public void run() {
    try {
      LOG.info("Loading image: {}", inputFile);
      var inputImage = imageService.read(Paths.get(inputFile));
      var dataframe = getDataframe(inputImage, imageToDataframe.convert(inputImage));
      LOG.info("Image loaded into dataframe");

      LOG.info("Training started");
      var predict = isolationForestService.perform(numberOfTrees, forestType, dataframe)
              .predict(dataframe);
      LOG.info("Training ended");

      LOG.info("Building output image");
      final double factor = fast ? scalingFactor : 1;
      int width = (int) (inputImage.getWidth() * factor);
      int height = (int) (inputImage.getHeight() * factor);
      int row = 0;
      int col = 0;
      var scores = predict.<Double>get("anomalies");

      final ToDoubleFunction<Double> identity = i -> i;
      double min = scores.stream().mapToDouble(identity).min().orElse(0);
      double max = scores.stream().mapToDouble(identity).max().orElse(1);
      var heatmap = heatMapFactory.generate(min, max);

      var outputImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
      for (double score : scores) {
        outputImage.setRGB(row, col, heatmap.getColor(score));
        if (row < height - 1) {
          ++row;
        } else {
          row = 0;
          ++col;
        }
      }
      LOG.info("Output image built");

      LOG.info("Writing output image {}", outputFile);
      imageService.write(Paths.get(outputFile), outputImage);
      LOG.info("output image written");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Dataframe getDataframe(BufferedImage image, Dataframe dataframe) {
    if (fast) {
      var resizedImage = imageService.resizeImage(image, scalingFactor);
      return imageToDataframe.convert(resizedImage);
    }
    return dataframe;
  }
}
