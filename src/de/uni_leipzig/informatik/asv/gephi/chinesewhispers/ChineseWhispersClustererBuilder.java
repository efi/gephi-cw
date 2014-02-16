package de.uni_leipzig.informatik.asv.gephi.chinesewhispers;

import org.gephi.clustering.spi.*;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ClustererBuilder.class)
public class ChineseWhispersClustererBuilder<T> implements ClustererBuilder {
    
    Clusterer clusterer = null;

    @Override
    public Clusterer getClusterer() {
        if (clusterer==null) {
            clusterer = new ChineseWhispersClusterer();
        }
        return clusterer;
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
