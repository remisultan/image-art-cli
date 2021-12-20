package org.rsultan.app;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.rsultan.app.convert.ImageToDataframe;
import org.rsultan.app.domain.KMedoidsWrapper;
import org.rsultan.app.service.ImageService;
import org.rsultan.core.dimred.PrincipalComponentAnalysis;
import org.rsultan.dataframe.Dataframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;

@Command(name = "pca", mixinStandardHelpOptions = true)
public class PCACommand extends IOFileCommand implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(PCACommand.class);

  private static final String N_DESC = "the number of components for your PCA";

  @Option(names = {"-n", "--number-of-components"}, description = N_DESC, required = true)
  int numberOfComponents;

  @Inject
  private ImageService imageService;

  @Inject
  private ImageToDataframe imageToDataframe;

  public static void main(String[] args) {
    PicocliRunner.run(PCACommand.class, args);
  }

  public void run() {
    try {
      LOG.info("Loading image: {}", inputFile);
      var inputImage = imageService.read(Paths.get(inputFile));
      var dataframe = imageToDataframe.convert(inputImage);
      LOG.info("Image loaded into dataframe");

      LOG.info("Loading training dataframe");
      var trainDataframe = getDataframe(inputImage, dataframe);
      LOG.info("Training dataframe loaded");

      LOG.info("Training started");
      final int colSize = trainDataframe.getColumnSize();
      final int nbComponent = numberOfComponents > colSize ? colSize : numberOfComponents;
      var PCA = new PrincipalComponentAnalysis(nbComponent)
              .setResponseVariable("response")
              .train(trainDataframe);
      PCA.predict(trainDataframe).tail();
      LOG.info("Training ended");

      LOG.info("Building output image");
      var outputImage = imageToDataframe.revert(PCA.reconstruct());
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
