package de.uni_leipzig.informatik.asv.gephi.chinesewhispers;

import java.util.*;
import org.gephi.clustering.api.Cluster;
import org.gephi.clustering.spi.Clusterer;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.HierarchicalUndirectedGraph;
import org.gephi.graph.api.Node;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.ProgressTicket;

public class ChineseWhispersClusterer implements Clusterer, LongTask {

    private List<Cluster> result = new ArrayList<Cluster>();
 
    private int iterations = 10;
    public int getIterations() { return iterations; }
    public void setIterations(int iterations) { this.iterations = Math.max(1,iterations); }
     
    private boolean randomColoring = true;
    public boolean getRandomColoring() { return randomColoring; }
    public void setRandomColoring(boolean randomColoring) { this.randomColoring = randomColoring; }
    
    public enum Propagation { TOP, DIST, DIST_LOG, VOTE }
    private Propagation propagation = Propagation.TOP;
    public Propagation getPropagation() { return propagation; }
    public void setPropagation( Propagation propagation ) { this.propagation = propagation; }
    
    private double propagationVoteValue = 0.1d;
    public double getPropagationVoteValue() { return propagationVoteValue; }
    public void setPropagationVoteValue(double propagationVoteValue) { this.propagationVoteValue = propagationVoteValue; }

    private boolean stepwiseUpdate = false;
    public boolean getStepwiseUpdate() { return stepwiseUpdate; }
    public void setStepwiseUpdate(boolean stepwiseUpdate) { this.stepwiseUpdate = stepwiseUpdate; }
    
    private double minimumEdgeWeight = 0.0d;
    public double getMinimumEdgeWeight() { return minimumEdgeWeight; }
    public void setMinimumEdgeWeight(double minimumEdgeWeight) { this.minimumEdgeWeight = minimumEdgeWeight; }
    
    private boolean randomizedNodeOrder = false;
    public boolean getRandomizedNodeOrder() { return randomizedNodeOrder; }
    public void setRandomizedNodeOrder(boolean randomizedNodeOrder) { this.randomizedNodeOrder = randomizedNodeOrder; }
    
    ProgressTicket progress = null;
    boolean cancelled = false;
    
    @Override
    public void execute(GraphModel gm) {
        cancelled = false;
        progress.start(2*iterations+2);           
        progress.progress("Clustering setup");
        result = new ArrayList<Cluster>();      
        
        HierarchicalUndirectedGraph graph = gm.getHierarchicalUndirectedGraph();
        Map<Node,Integer> classes = new HashMap<Node, Integer>();
        
        progress.progress("Filterung of unconnected Nodes");
        int counter = 0;
        List<Node> connectedNodes = new ArrayList<Node>();
        for (Node node : graph.getNodes()) {
          if (graph.getNeighbors(node).iterator().hasNext()) {
              connectedNodes.add(node);
              classes.put(node, counter++);
          }
        }
        progress.progress("Initializing clustering iterations");
        progress.progress();
        
        for (int i = 0; i<iterations; i++) {
            
            progress.progress("Clustering iteration "+i+": randomizing node order");
            progress.progress();
            
            Set<Node> inputNodes = new HashSet<Node>();
            if (randomizedNodeOrder) {
                inputNodes = new TreeSet<Node>(new Comparator<Node>(){
                    @Override
                    public int compare(Node o1, Node o2) {
                        int decision = (int)(2*Math.round(Math.random())-1);
                        return decision==0 ? 1 : decision;
                    }
                });
            }
            inputNodes.addAll(connectedNodes);
            
            progress.progress("Clustering iteration "+i+": propagating classes");
            progress.progress();           
            
            Map<Node,Integer> classesBuffer = new HashMap<Node, Integer>(classes);
            for (Node node : inputNodes) {
                if (cancelled) return;
                
                Map<Integer,Double> rankedClasses = new HashMap<Integer, Double>();
                
                for (Edge edge : graph.getEdges(node)) {
                    if (edge.getWeight()<minimumEdgeWeight) continue;
                    int neighbourClass;
                    Node otherNode = edge.getSource()==node ? edge.getTarget() : edge.getSource();
                    neighbourClass = classes.get(otherNode);
                    double impact=0;
                    switch (propagation) {
                        case TOP : case VOTE : impact = edge.getWeight();  break;
                        case DIST: impact = edge.getWeight()/graph.getDegree(otherNode); break;
                        case DIST_LOG : impact = edge.getWeight()/Math.log(1+graph.getDegree(otherNode)); break;
                    }
                    if (rankedClasses.get(neighbourClass)!=null) rankedClasses.put(neighbourClass, rankedClasses.get(neighbourClass)+impact);
                    else rankedClasses.put(neighbourClass,impact);
                }
                
                double sum = 0;
                int highestRankedClass = classes.get(node);
                double highestWeight = Double.MIN_VALUE;
                for ( Map.Entry<Integer, Double> entry : rankedClasses.entrySet()) {
                    sum += entry.getValue();
                    if (entry.getValue()>highestWeight) {
                        highestWeight = entry.getValue();
                        highestRankedClass = entry.getKey();
                    }
                }
                
                if (propagation != Propagation.VOTE || highestWeight/sum > propagationVoteValue) {                   
                    // sample mutation: (TODO: autoincrement new class)
                    //if (Math.random()<0.1-i/iterations/20) highestRankedClass = (int)Math.round(Math.random()*100000));                    
                    if (stepwiseUpdate) {
                        classesBuffer.put(node, highestRankedClass);
                    } else {
                        classes.put(node, highestRankedClass);
                    }
                }
            }
            if (stepwiseUpdate) classes = classesBuffer;
            if (cancelled) return;            
        }
        if (cancelled) return;
        
        Map<Integer,ChineseWhispersClusterImpl> classCluster = new HashMap<Integer, ChineseWhispersClusterImpl>();
        for (Map.Entry<Node,Integer> entry : classes.entrySet()) {
            int classValue = entry.getValue();
            if (! classCluster.containsKey(classValue)) {
                ChineseWhispersClusterImpl cluster = new ChineseWhispersClusterImpl();
                cluster.setName("Cluster for Class #"+classValue);
                classCluster.put(classValue, cluster);
            }
            classCluster.get(classValue).addNode(entry.getKey());     
        }
        
        if (cancelled) return;
        progress.progress();
        
        result.addAll(classCluster.values());
        
        long longer=189635271;
        if (randomColoring) for (Cluster c : result) {
            longer=(longer+189635273)%Long.MAX_VALUE;
            float r= ((float)(longer%255l))/255f;
            float g= ((float)((longer/255l)%255l))/255f;
            float b= ((float)((longer/255l/255l)%255l))/255f;
            for (Node n : c.getNodes()) {
                n.getNodeData().setColor(r,g,b);
            }
        }
        
        progress.finish("Finished");
    }
    
    @Override
    public Cluster[] getClusters() {
        if (result.isEmpty()) return null;
        return result.toArray(new Cluster[0]);
    }

    @Override
    public boolean cancel() {
        progress.finish("Cancelled");
        return cancelled = true;
    }

    @Override
    public void setProgressTicket(ProgressTicket progress) {
       this.progress = progress;
    }
    
}