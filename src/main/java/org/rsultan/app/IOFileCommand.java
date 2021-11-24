package org.rsultan.app;

import picocli.CommandLine.Option;

public abstract class IOFileCommand {

  private static final String FAST_DESC = "train on a resized image";
  private static final String SCALING_FACTOR_DESC =
          "the scaling factor of your image (useful only with -f) "
                  + "your images will get bigger if value > 1 and this won't be faster";

  @Option(
      names = {"-i", "--input-file"},
      description = "the input image you want to work onto",
      required = true
  )
  String inputFile;

  @Option(
      names = {"-o", "--output-file"},
      description = "the output image path",
      required = true
  )
  String outputFile;

  @Option(names = {"-f", "--fast"}, description = FAST_DESC)
  boolean fast;

  @Option(
          names = {"-s", "--scaling-factor"},
          description = SCALING_FACTOR_DESC,
          defaultValue = "0.1"
  )
  double scalingFactor;

}
