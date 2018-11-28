import java.awt.Color;
import java.util.ArrayList;

// represents the User in the Maze
public class Player extends MazeSquare {

  // creates a new Player object
  Player(int i, int j) {
    super(i, j, Color.BLUE);
  }

  // EFFECT: move the player based on the given key input
  void movePlayer(String ke, ArrayList<Edge> walls) {
    if (ke.equals("up")) {
      moveUp(walls);
    }
    else if (ke.equals("right")) {
      moveRight(walls);
    }
    else if (ke.equals("down")) {
      moveDown(walls);
    }
    else if (ke.equals("left")) {
      moveLeft(walls);
    }
  }

  // EFFECT: move the player up if the player is not at the edge and there is not
  // a wall there
  void moveUp(ArrayList<Edge> walls) {
    if (this.posn.y != 0 && wallDoesNotExist(this.posn.y, this.posn.y - 1)) {
      this.posn.y--;
    }
  }

  // EFFECT: move the player right if the player is not at the edge and there is
  // not a wall there
  void moveRight(ArrayList<Edge> walls) {
    if (this.posn.x != MazeWorld.WIDTH - 1) {
      this.posn.x++;
    }
  }

  // EFFECT: move the player down if the player is not at the edge and there is
  // not a wall there
  void moveDown(ArrayList<Edge> walls) {
    if (this.posn.y != MazeWorld.HEIGHT - 1) {
      this.posn.y++;
    }
  }

  // EFFECT: move the player left if the player is not at the edge and there is
  // not a wall there
  void moveLeft(ArrayList<Edge> walls) {
    if (this.posn.x != 0) {
      this.posn.x--;
    }
  }

  // not yet implemented
  boolean wallDoesNotExist(int start, int end) {
    return true;
  }

}
