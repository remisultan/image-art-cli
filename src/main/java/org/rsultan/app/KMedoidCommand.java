package org.rsultan.app;

import io.micronaut.configuration.picocli.PicocliRunner;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import javax.inject.Inject;
import org.rsultan.app.convert.ImageToDataframe;
import org.rsultan.app.convert.KMedoidToImage;
import org.rsultan.app.domain.KMedoidsWrapper;
import org.rsultan.app.service.ImageService;
import org.rsultan.app.service.KMedoidService;
import org.rsultan.core.clustering.type.MedoidType;
import org.rsultan.dataframe.Dataframe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "kmedoid", mixinStandardHelpOptions = true)
public class KMedoidCommand extends IOFileCommand implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(KMedoidCommand.class);

  private static final String MEDOID_DESC = "the type of KMedoid you want to apply";
  private static final String K_DESC = "the number of clusters you wish to have";
  private static final String EPOCHS_DESC = "the number of epochs you want to train";
  private static final String FAST_DESC = "train on a resized image";
  private static final String SCALING_FACTOR_DESC =
      "the scaling factor of your image (useful only with -f) "
          + "your images will get bigger if value > 1 and this won't be faster";

  @Option(names = {"-t", "--type"}, description = MEDOID_DESC, required = true)
  MedoidType medoidType;

  @Option(names = {"-k", "--k-clusters"}, description = K_DESC, required = true)
  int k;

  @Option(names = {"-e", "--epochs"}, description = EPOCHS_DESC, required = true)
  int epoch;

  @Option(names = {"-f", "--fast"}, description = FAST_DESC)
  boolean fast;

  @Option(
      names = {"-s", "--scaling-factor"},
      description = SCALING_FACTOR_DESC,
      defaultValue = "0.1"
  )
  double scalingFactor;

  @Inject
  private KMedoidService kMedoidsService;

  @Inject
  private ImageService imageService;

  @Inject
  private ImageToDataframe imageToDataframe;

  @Inject
  private KMedoidToImage kMedoidsToImage;

  public static void main(String[] args) {
    PicocliRunner.run(KMedoidCommand.class, args);
  }

  public void run() {
    try {
      LOG.info("Loading image: {}", inputFile);
      var inputImage = imageService.read(Paths.get(inputFile));
      var dataframe = imageToDataframe.convert(inputImage);
      LOG.info("Image loaded into dataframe");

      LOG.info("Loading training dataframe");
      var trainDataframe = getTrainDataframe(inputImage, dataframe);
      LOG.info("Training dataframe loaded");

      LOG.info("Training started");
      var kMedoids = kMedoidsService.perform(k, epoch, medoidType, trainDataframe);
      LOG.info("Training ended");

      LOG.info("Building output image");
      var outputImage = kMedoidsToImage.convert(new KMedoidsWrapper(
          kMedoids, dataframe, inputImage
      ));
      LOG.info("Output image built");

      LOG.info("Writing output image {}", outputFile);
      imageService.write(Paths.get(outputFile), outputImage);
      LOG.info("output image written");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Dataframe getTrainDataframe(BufferedImage image, Dataframe dataframe) {
    if (fast) {
      var resizedImage = imageService.resizeImage(image, scalingFactor);
      return imageToDataframe.convert(resizedImage);
    }
    return dataframe;
  }
}
