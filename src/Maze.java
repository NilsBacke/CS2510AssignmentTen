import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javalib.impworld.WorldScene;

// represents a Maze object
public class Maze {
  ArrayList<ArrayList<Vertex>> vertices; // the squares of the maze
  ArrayList<Edge> edges; // all possible edges
  ArrayList<Edge> walls; // the walls of the maze

  // constructs a new Maze object
  Maze(int width, int height) {
    vertices = new ArrayList<ArrayList<Vertex>>();
    int counter = 0;

    // initialize all of the vertices
    for (int i = 0; i < width; i++) {
      ArrayList<Vertex> sublist = new ArrayList<Vertex>();
      counter++;
      for (int j = 0; j < height; j++) {
        sublist.add(new Vertex(i, j, counter));
        counter++;
      }
      vertices.add(sublist);
    }

    vertices.get(0).get(0).color = Color.GREEN;
    vertices.get(width - 1).get(height - 1).color = Color.MAGENTA;

    // initialize all of the edges
    edges = new ArrayList<Edge>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        vertices.get(i).get(j).addEdges(vertices);
        edges.addAll(vertices.get(i).get(j).outEdges);
      }
    }

    this.walls = new ArrayList<Edge>();
    // perform kruskals
    kruskals();
    // set walls to the actual walls of the maze
    this.walls = getFinalEdges(this.edges, this.vertices);
  }

  // returns the edges of the final maze
  ArrayList<Edge> getFinalEdges(ArrayList<Edge> edges, ArrayList<ArrayList<Vertex>> vertices) {
    ArrayList<Edge> walls = new ArrayList<Edge>();
    for (Edge e : edges) {
      if (!containedInOutEdges(e, vertices)) {
        walls.add(e);
      }
    }
    return walls;
  }

  // return true if the given edge is contained in any of the vertices' outEdges
  boolean containedInOutEdges(Edge e, ArrayList<ArrayList<Vertex>> vertices) {
    boolean result = false;
    for (ArrayList<Vertex> list : vertices) {
      for (Vertex v : list) {
        // because of overriding equals method
        if (v.outEdges.contains(e)) {
          result = true;
        }
      }
    }
    return result;
  }

  // perform kruskals algorithm
  void kruskals() {
    HashMap<Integer, Integer> reps = new HashMap<>();
    // worklist = this.edges

    // get this.edges from every vertex's edge
    ArrayList<Edge> allPossibleEdges = new ArrayList<Edge>();
    for (ArrayList<Vertex> list : vertices) {
      for (Vertex v : list) {
        for (Edge e : v.outEdges) {
          allPossibleEdges.add(e);
        }
      }
    }

    sortEdges(allPossibleEdges);

    // fill the hashmap
    for (ArrayList<Vertex> list : vertices) {
      for (Vertex v : list) {
        v.outEdges = new ArrayList<Edge>();
        reps.put(v.id, v.id);
      }
    }

    // kruskal's
    while (numTrees(reps) > 1) {
      Edge edge = allPossibleEdges.get(0);
      if (find(reps, edge.from.id) == find(reps, edge.to.id)) {
        allPossibleEdges.remove(0);
      }
      else {
        union(reps, find(reps, edge.from.id), find(reps, edge.to.id));
        // add to edgesInTree
        // add both directions for the edge
        edge.from.outEdges.add(edge);
        edge.to.outEdges.add(new Edge(edge.to, edge.from, edge.weight));
      }
    }
  }

  // return the number of trees in the hashmap
  int numTrees(HashMap<Integer, Integer> reps) {
    int count = 0;

    for (Map.Entry<Integer, Integer> entry : reps.entrySet()) {
      if (entry.getValue() == entry.getKey()) {
        count++;
      }
    }
    return count;
  }

  // return the representative of the given key that refers to a Vertex
  int find(HashMap<Integer, Integer> representatives, int key) {
    if (key == (representatives.get(key))) {
      return key;
    }
    else {
      return this.find(representatives, representatives.get(key));
    }
  }

  // join the two keys into the same minimum spanning tree
  void union(HashMap<Integer, Integer> representatives, int key1, int key2) {
    representatives.put(key2, key1);
  }

  // EFFECT: perform selection sort on the given arraylist
  void sortEdges(ArrayList<Edge> edges) {
    for (int i = 0; i < edges.size() - 1; i++) {
      int minIndex = i;
      for (int j = i + 1; j < edges.size(); j++) {
        if (edges.get(j).weight < edges.get(minIndex).weight) {
          minIndex = j;
        }
      }
      Edge temp = edges.get(i);
      edges.set(i, edges.get(minIndex));
      edges.set(minIndex, temp);
    }
  }

  // EFFECT: add all of the vertices' and edges' images to the given WorldScene
  void render(WorldScene scene, Player player) {
    for (ArrayList<Vertex> list : vertices) {
      for (Vertex v : list) {
        v.addToScene(scene);
      }
    }
    
    player.addToScene(scene);

    for (Edge e : this.walls) {
      e.addToScene(scene);
    }
  }
}
