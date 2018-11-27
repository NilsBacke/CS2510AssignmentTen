import javalib.impworld.World;
import javalib.impworld.WorldScene;

public class MazeWorld extends World {
  static final int WIDTH = 25;
  static final int HEIGHT = 25;
  
  static final int WINDOW_WIDTH = WIDTH * Vertex.SIZE;
  static final int WINDOW_HEIGHT = HEIGHT * Vertex.SIZE;
  
  Maze maze;
  
  MazeWorld() {
    maze = new Maze(WIDTH, HEIGHT);
  }
  
  @Override
  public WorldScene makeScene() {
    return this.maze.render(this);
  }
}

class ExamplesMaze {
  public static void main(String[] argv) {
    // run the game
    MazeWorld w = new MazeWorld();
    w.bigBang(MazeWorld.WINDOW_WIDTH, MazeWorld.WINDOW_HEIGHT, 0.3);
  }
}
