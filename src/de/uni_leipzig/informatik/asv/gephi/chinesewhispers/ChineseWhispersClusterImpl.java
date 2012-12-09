package de.uni_leipzig.informatik.asv.gephi.chinesewhispers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gephi.clustering.api.Cluster;
import org.gephi.graph.api.Node;

public class ChineseWhispersClusterImpl implements Cluster {

    private List<Node> nodes = new ArrayList<Node>();
    private String clustername = "untitled";
    private Node metanode = null;

    public ChineseWhispersClusterImpl( ) { }  
    public ChineseWhispersClusterImpl( Node[] nodes ) {
        this.nodes = Arrays.asList(nodes);
    }
    public ChineseWhispersClusterImpl( List<Node> nodeList ) {
        this.nodes = nodeList;
    }
 
    public void addNode(Node node) {
        this.nodes.add(node);
    }
    
    public void setName(String clustername) {
        this.clustername = clustername;
    } 
    
    @Override
    public Node[] getNodes() {
        return this.nodes.toArray(new Node[0]);
    }

    @Override
    public int getNodesCount() {
        return this.nodes.size();
    }

    @Override
    public String getName() {
        return this.clustername;
    }

    @Override
    public Node getMetaNode() {
        return this.metanode;
    }

    @Override
    public void setMetaNode(Node node) {
        this.metanode = node;
    }
    
}
