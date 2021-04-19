package org.rsultan.app.convert;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.inject.Singleton;
import org.rsultan.dataframe.Column;
import org.rsultan.dataframe.Dataframe;
import org.rsultan.dataframe.Dataframes;

@Singleton
public class ImageToDataframe implements Converter<Dataframe, BufferedImage> {

  @Override
  public Dataframe convert(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();

    var red = new Column<Integer>("r", new ArrayList<>());
    var green = new Column<Integer>("g", new ArrayList<>());
    var blue = new Column<Integer>("b", new ArrayList<>());
    var alpha = new Column<Integer>("a", new ArrayList<>());

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        var color = new Color(image.getRGB(x, y), true);
        red.values().add(color.getRed());
        green.values().add(color.getGreen());
        blue.values().add(color.getBlue());
        alpha.values().add(color.getAlpha());
      }
    }

    return Dataframes.create(red, green, blue, alpha);
  }
}
