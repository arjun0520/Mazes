package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {

        Collection<EdgeWithData<Room, Wall>> edge = new LinkedList<>();
        for (Wall current : walls) {
            double currentRand = rand.nextDouble();
            edge.add(new EdgeWithData<>(current.getRoom1(), current.getRoom2(), currentRand, current));
        }
        Collection<EdgeWithData<Room, Wall>> mst =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edge)).edges();
        Set<Wall> hashSet = new HashSet<>();
        for (EdgeWithData<Room, Wall> current : mst) {
            hashSet.add(current.data());
        }
        return hashSet;
    }
}
