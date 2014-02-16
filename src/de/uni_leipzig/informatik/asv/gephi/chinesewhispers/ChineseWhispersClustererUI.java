package de.uni_leipzig.informatik.asv.gephi.chinesewhispers;

import javax.swing.JPanel;
import org.gephi.clustering.spi.Clusterer;
import org.gephi.clustering.spi.ClustererUI;

public class ChineseWhispersClustererUI implements ClustererUI {

    
    ChineseWhispersClusterer clusterer = null;
    ChineseWhispersPanel panel;
    
    @Override
    public JPanel getPanel() {
        panel = new ChineseWhispersPanel();
        return panel;
    }

    @Override
    public void setup(Clusterer clust) {
        clusterer = (ChineseWhispersClusterer)clust;
        if (panel != null) {
            panel.setUseVisibleGraph(clusterer.getUseVisibleGraph());
            panel.setIterations(clusterer.getIterations());
            panel.setDoColoring(clusterer.getRandomColoring());
            panel.setRandomNodeOrder(clusterer.getRandomizedNodeOrder());
            panel.setPropagation(clusterer.getPropagation());
            panel.setPropagationVoteValue((float)clusterer.getPropagationVoteValue());
            panel.setStepwiseUpdates(clusterer.getStepwiseUpdate());
            panel.setMiniumEdgeWeight((float) clusterer.getMinimumEdgeWeight());
        }
    }

    @Override
    public void unsetup() {
        if (panel != null) {
            clusterer.setUseVisibleGraph(panel.getUseVisibleGraph());
            clusterer.setIterations(panel.getIterations());
            clusterer.setRandomColoring(panel.getDoColoring());
            clusterer.setRandomizedNodeOrder(panel.getRandomNodeOrder());
            clusterer.setPropagation(panel.getPropagation());
            clusterer.setPropagationVoteValue(panel.getPropagationVoteValue());
            clusterer.setStepwiseUpdate(panel.getStepwiseUpdates());
            clusterer.setMinimumEdgeWeight(panel.getMinimumEdgeWeight());
        }
    }
    
}
