import java.awt.Color;
import java.util.ArrayList;

// represents a block in the maze
public class Vertex extends MazeSquare {
  int id;
  ArrayList<Edge> outEdges;
  static final int SIZE = 30;

  // creates a new Vertex object
  Vertex(int i, int j, int uniqueKey) {
    super(i, j, Color.GRAY);
    this.id = uniqueKey;
    this.outEdges = new ArrayList<Edge>();
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
}
