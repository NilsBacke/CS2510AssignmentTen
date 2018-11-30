import java.awt.Color;
import java.util.ArrayList;

// represents a block in the maze
public class Vertex extends MazeSquare {
  int id;
  ArrayList<Edge> outEdges;
  static final int SIZE = 20;
  boolean visited;
  boolean onPath;

  // creates a new Vertex object
  Vertex(int i, int j, int uniqueKey) {
    super(i, j, Color.GRAY);
    this.id = uniqueKey;
    this.outEdges = new ArrayList<Edge>();
    this.visited = false;
    this.onPath = false;
  }

  // EFFECT: all of the appropriate edges to the outEdges list
  void addEdges(ArrayList<ArrayList<Vertex>> vertices) {
    int i = this.posn.x;
    int j = this.posn.y;
    if (i != 0) {
      this.outEdges.add(new Edge(this, vertices.get(i - 1).get(j)));
    }
    if (i != vertices.size() - 1) {
      this.outEdges.add(new Edge(this, vertices.get(i + 1).get(j)));
    }
    if (j != 0) {
      this.outEdges.add(new Edge(this, vertices.get(i).get(j - 1)));
    }
    if (j != vertices.get(0).size() - 1) {
      this.outEdges.add(new Edge(this, vertices.get(i).get(j + 1)));
    }
  }

  // EFFECT: set the visited flag and color of this vertex
  void setVisited() {
    this.visited = true;
    this.color = new Color(50, 50, 170);
  }

  // compares the two posns
  public boolean equals(Object o) {
    if (o instanceof Vertex) {
      return this.posn.equals(((Vertex) o).posn);
    }
    return false;
  }

  // returns the unique ID
  public int hashCode() {
    return this.id;
  }

  // string representation of this vertex (the posn)
  public String toString() {
    return this.posn.toString();
  }
}
