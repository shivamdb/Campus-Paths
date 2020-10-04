package pathfinder;

import graph.DirectedGraph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * This class finds the shortest distance between 2 nodes, if any shortest distance
 * @param <T> The node type of th graph
 */
public class ShortestPath<T> {

    /**
     * Finds the shortest path between any two nodes, if any
     * @param g the graph to find shortest distance on between 2 nodes
     * @param start node from which to start
     * @param dest node from which to end
     * @return return the shortest path found, if any
     *          returns null if no path found
     * @spec.requires g!=null and start!=null and dest!=null
     */
    public Path<T> findShortestPath(DirectedGraph<T, Double> g, T start, T dest) {
        PriorityQueue<Path<T>> p  = new PriorityQueue<>(new PathComparator());
        Set<T> finished = new HashSet<>();
        p.add(new Path<>(start));

        while(!p.isEmpty()) {
            Path<T> minPath = p.remove();
            T minDest = minPath.getEnd();

            // to check if node equal to destination
            if(minDest.equals(dest)) {
                return minPath; // returns the path found
            }
            // if node in the finished set doesn't check
            if(finished.contains(minDest)) {
                continue;
            }

            List<DirectedGraph<T, Double>.WeightedEdge<T,Double>> edges = g.childrenAndEdges(minDest);

            for(DirectedGraph<T, Double>.WeightedEdge<T, Double> children : edges) {
                if(!finished.contains(children.getChild())) {
                    // looks for path and add
                    Path<T> newPath = minPath.extend(children.getChild(), children.getEdge());
                    p.add(newPath);
                }
            }
            // add node to finished path
            finished.add(minDest);
        }
        return null; // if no path found
    }

    /**
     * A class to compare two paths based on their weights
     */
    private class PathComparator implements Comparator<Path<T>> {
        /**
         * comparesTo path based on their cost
         * @param p1 the 1st
         * @param p2 The 2nd path
         * @return 1 if p1.cost > p2.cost
         *         0 if p1.cost = p2.cost
         *         -1 if p1.cost < p2.cost
         */
        public int compare(Path<T> p1, Path<T> p2) {
            return Double.compare(p1.getCost(), p2.getCost());
        }
    }
}
