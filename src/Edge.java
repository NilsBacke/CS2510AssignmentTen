import java.awt.Color;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Edge {
  Vertex from, to;
  int weight;

  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    Random rand = new Random();
    this.weight = rand.nextInt(MazeWorld.HEIGHT * MazeWorld.WIDTH * 25);
  }

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

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

  // returns a WorldImage that represents a horizontal line
  WorldImage getVerticalLine() {
    return new LineImage(new Posn(0, Vertex.SIZE), Color.BLACK);
  }

  WorldImage getHorizontalLine() {
    return new LineImage(new Posn(Vertex.SIZE, 0), Color.BLACK);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Edge) {
      Edge e = (Edge) o;
      return (this.from.equals(e.from) && this.to.equals(e.to) && this.weight == e.weight)
          || (this.to == e.from && this.from == e.to);
    }
    return false;
  }
}
