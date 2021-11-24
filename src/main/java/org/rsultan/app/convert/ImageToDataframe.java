package org.rsultan.app.convert;


import org.rsultan.dataframe.Column;
import org.rsultan.dataframe.Dataframe;
import org.rsultan.dataframe.Dataframes;

import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ImageToDataframe implements Converter<Dataframe, BufferedImage> {

    @Override
    public Dataframe convert(BufferedImage image) {
        var columns = new ArrayList<Column<Integer>>();
        final int w = image.getWidth();
        final int h = image.getHeight();
        for (int x = 0; x <= w; x++) {
            List<Integer> col = new ArrayList<>();
            for (int y = 0; y < h; y++) {
                if (x == w) {
                    col.add(-1);
                } else {
                    col.add(image.getRGB(y, x));
                }
            }
            columns.add(new Column<>((x == w ? "response" : "col_" + x), col));
        }
        return Dataframes.create(columns.toArray(Column[]::new));
    }

    @Override
    public BufferedImage revert(Dataframe dataframe) {
        var resizedImage = new BufferedImage(dataframe.getRowSize(), dataframe.getColumnSize(), BufferedImage.TYPE_INT_ARGB);
        final int w = resizedImage.getWidth();
        final int h = resizedImage.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                var value = (Number) dataframe.getColumns()[y].values().get(x);
                resizedImage.setRGB(x, y, value.intValue());
            }
        }
        return resizedImage;
    }
}
