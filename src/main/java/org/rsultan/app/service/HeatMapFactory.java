package org.rsultan.app.service;


import javax.inject.Singleton;
import java.awt.Color;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

@Singleton
public class HeatMapFactory {

    private static final List<Integer> colors = List.of(
            new Color(255,255, 0).getRGB(),
            new Color(255,235, 0).getRGB(),
            new Color(255,214, 0).getRGB(),
            new Color(255,192, 0).getRGB(),
            new Color(255,170, 0).getRGB(),
            new Color(255,147, 0).getRGB(),
            new Color(255,123, 0).getRGB(),
            new Color(255,96, 0).getRGB(),
            new Color(255,65, 0).getRGB(),
            new Color(255,0, 0).getRGB()
    );

    public HeatMap generate(double min, double max){
        double step = (max - min) / (double) colors.size();
        double key = min - step;
        var map = new TreeMap<Double, Integer>();
        for (Integer rgbInt : colors) {
            map.put(key, rgbInt);
            key += step;
        }
        return new HeatMap(map);
    }

    public record HeatMap(TreeMap<Double, Integer> heatmap){

        public Integer getColor(double score){
            for (Entry<Double, Integer> e : heatmap.entrySet()) {
                if (score <= e.getKey()){
                    return e.getValue();
                }
            }
            return colors.get(colors.size() -1);
        }
    }
}
