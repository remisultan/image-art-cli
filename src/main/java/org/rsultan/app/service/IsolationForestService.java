package org.rsultan.app.service;

import org.rsultan.core.clustering.ensemble.isolationforest.ExtendedIsolationForest;
import org.rsultan.core.clustering.ensemble.isolationforest.IsolationForest;
import org.rsultan.core.clustering.kmedoids.KMeans;
import org.rsultan.core.clustering.kmedoids.KMedians;
import org.rsultan.core.clustering.kmedoids.KMedoids;
import org.rsultan.core.clustering.type.MedoidType;
import org.rsultan.dataframe.Dataframe;

import javax.inject.Singleton;

@Singleton
public class IsolationForestService {

  public IsolationForest perform(int n, IForestType type, Dataframe df) {
    IsolationForest iForest = switch (type) {
      case REGULAR -> new IsolationForest(n);
      case EXTENDED -> new ExtendedIsolationForest(n, df.getColumnSize() - 1);
    };
    return iForest.setUseAnomalyScoresOnly(true).train(df);
  }

  public enum IForestType{
    REGULAR, EXTENDED
  }
}
