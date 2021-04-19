package org.rsultan.app.domain;

import java.awt.image.BufferedImage;
import org.rsultan.core.clustering.kmedoids.KMedoids;
import org.rsultan.dataframe.Dataframe;

public record KMedoidsWrapper(KMedoids kMedoids, Dataframe testDataframe, BufferedImage image) {}
