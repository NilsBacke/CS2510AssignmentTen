import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import tester.Tester;

// represents the World class for the Maze game
public class MazeWorld extends World {
  static final int WIDTH = 25;
  static final int HEIGHT = 25;

  static final int WINDOW_WIDTH = WIDTH * Vertex.SIZE;
  static final int WINDOW_HEIGHT = HEIGHT * Vertex.SIZE;

  Maze maze;
  Player player;

  // creates a new MazeWorld object
  MazeWorld() {
    maze = new Maze(WIDTH, HEIGHT);
    player = new Player(0, 0);
  }

  // draws all of the elements of the maze
  @Override
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    this.maze.render(scene, player); // also draws player
    return scene;
  }
  
  @Override 
  public void onTick() {
    Vertex currentPlayerSquare = this.maze.vertices.get(player.posn.x).get(player.posn.y);
    currentPlayerSquare.color = Color.YELLOW;
    currentPlayerSquare.visited = true;
  }

  // moves the Player when the player presses an arrow key
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      maze = new Maze(WIDTH, HEIGHT);
      player = new Player(0, 0);
    }
    else {
      player.movePlayer(ke, this.maze.walls);
    }
  }

  // trigger world end
  public WorldEnd worldEnds() {
    // check if user wins
    if (player.completed()) {
      // user wins
      return new WorldEnd(true, this.lastScene("NICE JOB"));
    }
    else {
      // game isn't over yet
      return new WorldEnd(false, this.makeScene());
    }
  }

  // produces the last image of this world by adding a box and text to the image
  public WorldScene lastScene(String s) {
    WorldScene scene = this.makeScene();
    scene.placeImageXY(new RectangleImage(80, 30, "solid", Color.white), WINDOW_WIDTH / 2,
        WINDOW_HEIGHT / 2);
    scene.placeImageXY(new TextImage(s, Color.red), WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
    return scene;
  }
}

// the tests for the MazeWorld game
class ExamplesMaze {

  MazeWorld world = new MazeWorld();
  Maze maze = new Maze(10, 10);
  HashMap<Integer, Integer> reps;
  HashMap<Integer, Integer> reps2;

  void reset() {
    reps = new HashMap<>();
    int counter = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        reps.put(counter, counter);
        counter++;
      }
    }
    reps2 = new HashMap<>();
    int counter2 = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        reps2.put(counter2, counter2 + 1);
        counter2++;
      }
    }
  }

  void testGetFinalEdges(Tester t) {
    ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>();
    ArrayList<Edge> empty = new ArrayList<Edge>();
    ArrayList<Edge> edges1 = new ArrayList<Edge>();
    ArrayList<Edge> edges2 = new ArrayList<Edge>();

    for (int i = 0; i < 10; i++) {
      ArrayList<Vertex> sublist = new ArrayList<Vertex>();
      for (int j = 0; j < 10; j++) {
        Vertex v = new Vertex(i, j, 0);
        v.outEdges.add(new Edge(v, v, 0));
        sublist.add(v);
      }
      vertices.add(sublist);

    }

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        edges1.add(new Edge(vertices.get(i).get(j), vertices.get(i).get(j), 0));
      }
    }

    t.checkExpect(maze.getFinalEdges(edges1, vertices), empty);
    // clear all outEdges
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        vertices.get(i).get(j).outEdges.clear();
      }
    }
    t.checkExpect(maze.getFinalEdges(empty, vertices), empty);
  }

  void testContainedInOutEdges(Tester t) {
    ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>();
    for (int i = 0; i < 10; i++) {
      ArrayList<Vertex> sublist = new ArrayList<Vertex>();
      for (int j = 0; j < 10; j++) {
        Vertex v = new Vertex(i, j, 0);
        v.outEdges.add(new Edge(v, v, 0));
        sublist.add(v);
      }
      vertices.add(sublist);

    }

    t.checkExpect(maze.containedInOutEdges(
        new Edge(vertices.get(0).get(0), vertices.get(0).get(0), 0), vertices), true);
    t.checkExpect(maze.containedInOutEdges(
        new Edge(vertices.get(1).get(1), vertices.get(0).get(0), 0), vertices), false);
    t.checkExpect(maze.containedInOutEdges(
        new Edge(vertices.get(5).get(5), vertices.get(5).get(5), 0), vertices), true);
    // ignore weights
    t.checkExpect(maze.containedInOutEdges(
        new Edge(vertices.get(0).get(0), vertices.get(0).get(0), 5), vertices), true);
  }

  void testNumTrees(Tester t) {
    reset();
    t.checkExpect(maze.numTrees(reps), 100);
    reset();
    t.checkExpect(maze.numTrees(reps2), 0);
  }

  void testFind(Tester t) {
    reset();
    t.checkExpect(maze.find(reps, 0), 0);
    t.checkExpect(maze.find(reps, 50), 50);
  }

  void testUnion(Tester t) {
    reset();
    maze.union(reps, 0, 0);
    t.checkExpect(reps.get(0), 0);
    reset();
    maze.union(reps, 50, 0);
    t.checkExpect(reps.get(0), 50);
  }

  void testSortEdges(Tester t) {
    ArrayList<Edge> edges1 = new ArrayList<Edge>();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        Vertex v = new Vertex(0, 0, 0);
        edges1.add(new Edge(v, v, i));
      }
    }
    maze.sortEdges(edges1);
    t.checkExpect(edges1.get(0).weight, 0);
    t.checkExpect(edges1.get(edges1.size() - 1).weight, 9);
  }

  void testGetVerticalLine(Tester t) {
    Edge e = new Edge();
    t.checkExpect(e.getVerticalLine(), new LineImage(new Posn(0, Vertex.SIZE), Color.BLACK));
  }

  void testGetHorizontalLine(Tester t) {
    Edge e = new Edge();
    t.checkExpect(e.getHorizontalLine(), new LineImage(new Posn(Vertex.SIZE, 0), Color.BLACK));
  }

  void testEdgeEquals(Tester t) {
    Vertex v1 = new Vertex(0, 0, 0);
    Vertex v2 = new Vertex(0, 1, 1);
    Vertex v3 = new Vertex(0, 1, 0);
    Vertex v4 = new Vertex(0, 1, 2);
    Edge e1 = new Edge(v1, v2);
    Edge e2 = new Edge(v1, v3);
    Edge e3 = new Edge(v2, v3);
    Edge e4 = new Edge(v2, v4);
    t.checkExpect(e1.equals(e2), false);
    t.checkExpect(e2.equals(e3), false);
    t.checkExpect(e1.equals(e3), false);
    t.checkExpect(e3.equals(e4), false);
    t.checkExpect(e4.equals(e4), true);
  }

  void testAddEdge(Tester t) {
    ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>();
    for (int i = 0; i < 10; i++) {
      ArrayList<Vertex> sublist = new ArrayList<Vertex>();
      for (int j = 0; j < 10; j++) {
        Vertex v = new Vertex(i, j, 0);
        sublist.add(v);
      }
      vertices.add(sublist);

    }

    t.checkExpect(vertices.get(0).get(0).outEdges.size(), 0);
    vertices.get(0).get(0).addEdges(vertices);
    t.checkExpect(vertices.get(0).get(0).outEdges.size(), 2);

    t.checkExpect(vertices.get(5).get(5).outEdges.size(), 0);
    vertices.get(5).get(5).addEdges(vertices);
    t.checkExpect(vertices.get(5).get(5).outEdges.size(), 4);
  }

  public static void main(String[] argv) {
    // run the game
    MazeWorld w = new MazeWorld();
    w.bigBang(MazeWorld.WINDOW_WIDTH, MazeWorld.WINDOW_HEIGHT, 0.05);
  }
}
