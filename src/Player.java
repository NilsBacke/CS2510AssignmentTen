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
  
  // returns true if the player has completed the maze
  boolean completed() {
    return this.posn.x == MazeWorld.WIDTH - 1 && this.posn.y == MazeWorld.HEIGHT - 1;
  }

  // EFFECT: move the player up if the player is not at the edge and there is not
  // a wall there
  void moveUp(ArrayList<Edge> walls) {
    if (this.posn.y != 0 && wallDoesNotExist("vertical", this.posn.y, this.posn.y - 1, walls)) {
      this.posn.y--;
    }
  }

  // EFFECT: move the player right if the player is not at the edge and there is
  // not a wall there
  void moveRight(ArrayList<Edge> walls) {
    if (this.posn.x != MazeWorld.WIDTH - 1
        && wallDoesNotExist("horizontal", this.posn.x, this.posn.x + 1, walls)) {
      this.posn.x++;
    }
  }

  // EFFECT: move the player down if the player is not at the edge and there is
  // not a wall there
  void moveDown(ArrayList<Edge> walls) {
    if (this.posn.y != MazeWorld.HEIGHT - 1
        && wallDoesNotExist("vertical", this.posn.y, this.posn.y + 1, walls)) {
      this.posn.y++;
    }
  }

  // EFFECT: move the player left if the player is not at the edge and there is
  // not a wall there
  void moveLeft(ArrayList<Edge> walls) {
    if (this.posn.x != 0 && wallDoesNotExist("horizontal", this.posn.x, this.posn.x - 1, walls)) {
      this.posn.x--;
    }
  }

  // returns true if there is not a wall connecting start and end indices in the
  // given direction
  boolean wallDoesNotExist(String direction, int start, int end, ArrayList<Edge> walls) {
    if (direction.equals("vertical")) {
      for (Edge e : walls) {
        if (e.from.posn.x == this.posn.x && e.to.posn.x == this.posn.x
            && ((e.from.posn.y == start && e.to.posn.y == end)
                || (e.to.posn.y == start && e.from.posn.y == end))) {
          return false;
        }
      }
    }
    else if (direction.equals("horizontal")) {
      for (Edge e : walls) {
        if (e.from.posn.y == this.posn.y && e.to.posn.y == this.posn.y
            && ((e.from.posn.x == start && e.to.posn.x == end)
                || (e.to.posn.x == start && e.from.posn.x == end))) {
          return false;
        }
      }
    }
    return true;
  }

}
