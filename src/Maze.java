import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javalib.impworld.World;
import javalib.impworld.WorldScene;

public class Maze {
  ArrayList<ArrayList<Vertex>> vertices;
  ArrayList<Edge> edges;
  ArrayList<Edge> walls;

  Maze(int width, int height) {
    vertices = new ArrayList<ArrayList<Vertex>>();
    int counter = 0;
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

    edges = new ArrayList<Edge>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        vertices.get(i).get(j).addEdges(vertices);
        edges.addAll(vertices.get(i).get(j).outEdges);
      }
    }

    this.walls = new ArrayList<Edge>();
    kruskals();
    this.walls = getFinalEdges(this.edges, this.vertices);
  }

  ArrayList<Edge> getFinalEdges(ArrayList<Edge> edges, ArrayList<ArrayList<Vertex>> vertices) {
    ArrayList<Edge> walls = new ArrayList<Edge>();
    for (Edge e : edges) {
      boolean notContainedInOutEdges = true;
      for (ArrayList<Vertex> list : vertices) {
        for (Vertex v : list) {
          // because of overriding equals method
          if (v.outEdges.contains(e)) {
            notContainedInOutEdges = false;
          }
        }
      }
      if (notContainedInOutEdges) {
        walls.add(e);
      }
    }

    return walls;
  }

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

    for (ArrayList<Vertex> list : vertices) {
      for (Vertex v : list) {
        v.outEdges = new ArrayList<Edge>();
        reps.put(v.id, v.id);
      }
    }

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

  int numTrees(HashMap<Integer, Integer> reps) {
    int count = 0;

    for (Map.Entry<Integer, Integer> entry : reps.entrySet()) {
      if (entry.getValue() == entry.getKey()) {
        count++;
      }
    }
    return count;
  }

  int find(HashMap<Integer, Integer> representatives, int key) {
    if (key == (representatives.get(key))) {
      return key;
    }
    else {
      return this.find(representatives, representatives.get(key));
    }
  }

  void union(HashMap<Integer, Integer> representatives, int key1, int key2) {
    representatives.put(key2, key1);
  }

  void sortEdges(ArrayList<Edge> edges) {
    for (int i = 0; i < edges.size() - 1; i++) {
      int minIndex = i;
      for (int j = i + 1; j < edges.size(); j++) {
        if (edges.get(j).weight < edges.get(minIndex).weight) {
          minIndex = j;
        }
      }
      swapEdges(edges, minIndex, i);
    }
  }

  void swapEdges(ArrayList<Edge> edges, int i, int j) {
    Edge temp = edges.get(j);
    edges.set(j, edges.get(i));
    edges.set(i, temp);
  }

  WorldScene render(World world) {
    WorldScene scene = world.getEmptyScene();

    for (ArrayList<Vertex> list : vertices) {
      for (Vertex v : list) {
        v.addToScene(scene);
      }
    }

    for (Edge e : this.walls) {
      e.addToScene(scene);
    }

    return scene;
  }
}
