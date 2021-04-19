package org.rsultan.app.service;

import javax.inject.Singleton;
import org.rsultan.core.clustering.kmedoids.KMeans;
import org.rsultan.core.clustering.kmedoids.KMedians;
import org.rsultan.core.clustering.kmedoids.KMedoids;
import org.rsultan.core.clustering.type.MedoidType;
import org.rsultan.dataframe.Dataframe;

@Singleton
public class KMedoidService {

  public KMedoids perform(int k, int epochs, MedoidType medoidType, Dataframe df) {
    KMedoids kMedoids = switch (medoidType) {
      case MEAN -> new KMeans(k, epochs);
      case MEDIAN -> new KMedians(k, epochs);
    };
    return kMedoids.train(df);
  }
}
