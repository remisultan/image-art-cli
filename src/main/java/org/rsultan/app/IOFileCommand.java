package org.rsultan.app;

import picocli.CommandLine.Option;

public abstract class IOFileCommand {

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

}
