package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.LinkedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        if (edges.isEmpty() && (graph.allVertices().isEmpty() || graph.allVertices().size() == 1)) {
            return new MinimumSpanningTree.Success<>();
        } else if (edges.isEmpty() && !graph.allVertices().isEmpty()) {
            return new MinimumSpanningTree.Failure<>();
        }
        edges.sort(Comparator.comparingDouble(E::weight));
        DisjointSets<V> disjointSets = createDisjointSets();
        int vertexCount = 0;
        int edgeCount = 0;
        List<E> toReturn = new LinkedList<>();
        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
            vertexCount += 1;
        }
        for (E edge : edges) {
            V from = edge.from();
            V to = edge.to();
            if (disjointSets.findSet(from) != disjointSets.findSet(to)) {
                disjointSets.union(from, to);
                edgeCount += 1;
                toReturn.add(edge);
            }
        }
        if (edgeCount == (vertexCount - 1)) {
            return new MinimumSpanningTree.Success<>(toReturn);
        }
        return new MinimumSpanningTree.Failure<>();
    }
}
