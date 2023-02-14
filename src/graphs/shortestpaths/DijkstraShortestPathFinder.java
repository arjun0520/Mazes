package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.Collections;
import java.util.Objects;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();
        distTo.put(start, 0.0);
        ExtrinsicMinPQ<V> perimeter = createMinPQ();
        perimeter.add(start, 0.0);
        while (!perimeter.isEmpty()) {
            V u = perimeter.removeMin();
            if (Objects.equals(u, end)) {
                break;
            }
            for (E v : graph.outgoingEdgesFrom(u)) {
                double weight = v.weight();
                if (distTo.containsKey(v.to())) {
                    double oldDist = distTo.get(v.to());
                    double newDist = distTo.get(u) + weight;
                    if (newDist < oldDist) {
                        distTo.put(v.to(), newDist);
                        edgeTo.put(v.to(), v);
                        perimeter.changePriority(v.to(), newDist);
                    }
                } else {
                    distTo.put(v.to(), distTo.get(u) + weight);
                    edgeTo.put(v.to(), v);
                    perimeter.add(v.to(), distTo.get(v.to()));
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        List<E> toReturn = new LinkedList<>();
        V current = end;
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (spt.get(end) == null) {
            return new ShortestPath.Failure<>();
        }
        while (!Objects.equals(current, start)) {
            E edge = spt.get(current);
            V from = edge.from();
            toReturn.add(edge);
            current = from;
        }
        Collections.reverse(toReturn);
        return new ShortestPath.Success<>(toReturn);
    }
}
