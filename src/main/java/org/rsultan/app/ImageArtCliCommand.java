package org.rsultan.app;

import io.micronaut.configuration.picocli.PicocliRunner;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.factory.Nd4j;
import picocli.CommandLine.Command;

@Command(name = "image-art", subcommands = {KMedoidCommand.class, PCACommand.class}, mixinStandardHelpOptions = true)
public class ImageArtCliCommand implements Runnable {

  public static void main(String[] args) {
    Nd4j.setDefaultDataTypes(DataType.DOUBLE, DataType.DOUBLE);
    PicocliRunner.run(ImageArtCliCommand.class, args);
  }

  public void run() {}
}
