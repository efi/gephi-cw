package de.uni_leipzig.informatik.asv.gephi.chinesewhispers;

import org.gephi.clustering.spi.*;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ClustererBuilder.class)
public class ChineseWhispersClustererBuilder<T> implements ClustererBuilder {

    @Override
    public Clusterer getClusterer() {
        return new ChineseWhispersClusterer();
    }

    @Override
    public String getName() {
        return "Chinese Whispers";
    }

    @Override
    public String getDescription() {
        return "Chinese Whispers Clustering";
    }

    @Override
    public Class getClustererClass() {
        return ChineseWhispersClusterer.class;
    }

    @Override
    public ClustererUI getUI() {
        return new ChineseWhispersClustererUI();
    }
    
}
