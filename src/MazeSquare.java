import java.awt.Color;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

// represents a square in the Maze
public class MazeSquare {
  Posn posn;
  Color color;

  // creates a new MazeSquare object
  MazeSquare(int i, int j, Color color) {
    this.posn = new Posn(i, j);
    this.color = color;
  }

  // adds this maze square to the given WorldScene
  void addToScene(WorldScene scene) {
    scene.placeImageXY(this.getImage(), this.getX(), this.getY());
  }

  // return the x coordinate of this square
  int getX() {
    return this.posn.x * Vertex.SIZE + Vertex.SIZE / 2;
  }

  // return the y coordinate of this square
  int getY() {
    return this.posn.y * Vertex.SIZE + Vertex.SIZE / 2;
  }

  // return the WorldImage of this MazeSquare
  WorldImage getImage() {
    return new RectangleImage(Vertex.SIZE, Vertex.SIZE, OutlineMode.SOLID, this.color);
  }
}
