package de.uni_leipzig.informatik.asv.gephi.chinesewhispers;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import org.gephi.clustering.spi.Clusterer;
import org.gephi.clustering.spi.ClustererUI;

public class ChineseWhispersClustererUI implements ClustererUI {

    JPanel panel = null;
    JTextField iterations = null;
    JCheckBox randomColoring = null;
    JRadioButton propagationTop = null;
    JRadioButton propagationDist = null;
    JRadioButton propagationDistLog = null;
    JRadioButton propagationVote = null;
    JTextField propagationVoteValue = null;
    JCheckBox stepwiseUpdate = null;
    JTextField minimumEdgeWeight = null;
    JCheckBox randomizedNodeOrder = null;
    // continuousUpdate
    // keepClassProbability
    // mutation pa ra meters
    
    ChineseWhispersClusterer clusterer = null;
    
    public ChineseWhispersClustererUI() {
        initComponents();
    }
     
    private JPanel configLine(String labelText, JComponent component) {
        JPanel configLine = new JPanel();
        configLine.setAlignmentX(Component.LEFT_ALIGNMENT);
        configLine.setLayout(new BoxLayout(configLine, BoxLayout.X_AXIS));
        JLabel label = new JLabel(labelText+":",JLabel.RIGHT);
        configLine.add(label);
        configLine.add(Box.createRigidArea(new Dimension(8,1)));
        configLine.add(component);
        configLine.add(Box.createHorizontalGlue());       
        return configLine;
    }
    
    private void initComponents() {
        panel = new JPanel(true);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(configLine("Iterations",iterations = new JTextField()));
        
        panel.add(configLine("Perform random coloring",randomColoring = new JCheckBox()));
        
        panel.add(new JSeparator());
       
        ButtonGroup propagationGroup = new ButtonGroup();
        JPanel propagationList = new JPanel();
        propagationList.setLayout(new GridLayout(4,1));
        propagationTop=new JRadioButton("top");
        propagationTop.setMnemonic(KeyEvent.VK_T);
        propagationTop.setSelected(true);
        propagationGroup.add(propagationTop);
        propagationList.add(propagationTop);
        propagationDist=new JRadioButton("dist");
        propagationDist.setMnemonic(KeyEvent.VK_D);
        propagationGroup.add(propagationDist);
        propagationList.add(propagationDist);
        propagationDistLog=new JRadioButton("dist log");
        propagationDistLog.setMnemonic(KeyEvent.VK_L);
        propagationGroup.add(propagationDistLog);
        propagationList.add(propagationDistLog);
        propagationVote=new JRadioButton("vote");
        propagationVote.setMnemonic(KeyEvent.VK_V);
        propagationGroup.add(propagationVote);
        propagationList.add(propagationVote);
        panel.add(configLine("Class propagation",propagationList));
        
        panel.add(configLine("Class propagation vote value",propagationVoteValue = new JTextField()));
        
        panel.add(configLine("Stepwise updates",stepwiseUpdate = new JCheckBox()));
        
        panel.add(configLine("Minimum edge weight",minimumEdgeWeight = new JTextField()));
        
        panel.add(new JSeparator());
        
        panel.add(configLine("Randomized node order",randomizedNodeOrder = new JCheckBox()));
        
    }
    
    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void setup(Clusterer clusterer) {
        this.clusterer = (ChineseWhispersClusterer)clusterer;
        iterations.setText(""+this.clusterer.getIterations());
        randomColoring.setSelected(this.clusterer.getRandomColoring());
        switch (this.clusterer.getPropagation()){
            case TOP : propagationTop.setSelected(true); break;
            case DIST : propagationDist.setSelected(true); break;
            case DIST_LOG : propagationDistLog.setSelected(true); break;
            case VOTE : propagationVote.setSelected(true); break;
        }
        propagationVoteValue.setText(Double.toString(this.clusterer.getPropagationVoteValue()));
        stepwiseUpdate.setSelected(this.clusterer.getStepwiseUpdate());
        minimumEdgeWeight.setText(Double.toString(this.clusterer.getMinimumEdgeWeight()));
        randomizedNodeOrder.setSelected(this.clusterer.getRandomizedNodeOrder());
    }

    @Override
    public void unsetup() {
        try {
            clusterer.setIterations(Integer.parseInt(iterations.getText()));
        } catch (NumberFormatException nfex) { }
        clusterer.setRandomColoring(randomColoring.isSelected());
        clusterer.setPropagation(
            propagationTop.isSelected() ? ChineseWhispersClusterer.Propagation.TOP :
            propagationDist.isSelected() ? ChineseWhispersClusterer.Propagation.DIST :
            propagationDistLog.isSelected() ? ChineseWhispersClusterer.Propagation.DIST_LOG :
            propagationVote.isSelected() ? ChineseWhispersClusterer.Propagation.VOTE : null
        );
        try {
            clusterer.setPropagationVoteValue(Double.parseDouble(propagationVoteValue.getText()));
        } catch (NumberFormatException nfex) { }
        clusterer.setStepwiseUpdate(stepwiseUpdate.isSelected());
        try {
            clusterer.setMinimumEdgeWeight(Double.parseDouble(minimumEdgeWeight.getText()));
        } catch (NumberFormatException nfex) { }
        clusterer.setRandomizedNodeOrder(randomizedNodeOrder.isSelected());
    }
    
}
