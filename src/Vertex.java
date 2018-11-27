import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Vertex {
  int id;
  // use posn
  Posn posn;
  ArrayList<Edge> outEdges;
  static final int SIZE = 20;
  Color color;
  
  Vertex(int i, int j, int uniqueKey) {
    this.id = uniqueKey;
    this.posn = new Posn(i, j);
    this.outEdges = new ArrayList<Edge>();
    this.color = Color.GRAY;
  }
  
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
  
  void addToScene(WorldScene scene) {
    scene.placeImageXY(this.getImage(), this.getX(), this.getY());
  }
  
  int getX() {
    return this.posn.x * Vertex.SIZE + Vertex.SIZE / 2;
  }
  
  int getY() {
    return this.posn.y * Vertex.SIZE + Vertex.SIZE / 2;
  }
  
  WorldImage getImage() {
    return new RectangleImage(SIZE, SIZE, OutlineMode.SOLID, this.color);
  }
}
