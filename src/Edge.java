import java.awt.Color;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

// represents a wall in the maze
public class Edge {
  Vertex from;
  Vertex to;
  int weight;

  // creates a new Edge object with a random weight
  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    Random rand = new Random();
    this.weight = rand.nextInt(MazeWorld.HEIGHT * MazeWorld.WIDTH * 25);
  }

  // creates a new Edge object with a given weight
  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // for testing purposes only
  Edge() {

  }

  // EFFECT: add this edge's image to the given WorldScene
  void addToScene(WorldScene scene) {
    if (this.from.posn.x == this.to.posn.x) {
      // horizontal line
      scene.placeImageXY(this.getHorizontalLine(), this.from.posn.x * Vertex.SIZE + Vertex.SIZE / 2,
          (this.to.posn.y + this.from.posn.y) * Vertex.SIZE / 2 + Vertex.SIZE / 2);
    }
    else {
      // vertical line
      scene.placeImageXY(this.getVerticalLine(),
          (this.to.posn.x + this.from.posn.x) * Vertex.SIZE / 2 + Vertex.SIZE / 2,
          this.from.posn.y * Vertex.SIZE + Vertex.SIZE / 2);
    }
  }

  // returns a WorldImage that represents a vertical line
  WorldImage getVerticalLine() {
    return new LineImage(new Posn(0, Vertex.SIZE), Color.BLACK);
  }

  // returns a WorldImage that represents a horizontal line
  WorldImage getHorizontalLine() {
    return new LineImage(new Posn(Vertex.SIZE, 0), Color.BLACK);
  }

  // returns true if this edge is equal to the given object
  @Override
  public boolean equals(Object o) {
    if (o instanceof Edge) {
      Edge e = (Edge) o;
      return (this.from.equals(e.from) && this.to.equals(e.to) && this.weight == e.weight)
          || (this.to == e.from && this.from == e.to); // ignore weights
    }
    return false;
  }
  
  // returns a unique hashcode
  @Override
  public int hashCode() {
    return this.from.posn.x * MazeWorld.WIDTH + this.from.posn.y;
  }
}
