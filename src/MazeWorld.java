import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.LineImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldEnd;
import javalib.worldimages.WorldImage;
import tester.Tester;

// represents the World class for the Maze game
public class MazeWorld extends World {
  static final int WIDTH = 25;
  static final int HEIGHT = 25;

  static final int WINDOW_WIDTH = WIDTH * Vertex.SIZE;
  static final int WINDOW_HEIGHT = HEIGHT * Vertex.SIZE;

  Maze maze;
  Player player;

  String mode; // either "player", "dfs", or "bfs"

  // creates a new MazeWorld object
  MazeWorld() {
    this.maze = new Maze(WIDTH, HEIGHT);
    this.player = new Player(0, 0);
    this.mode = "player";
  }

  // draws all of the elements of the maze
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    this.maze.render(scene, player); // also draws player
    return scene;
  }

  // every game tick
  public void onTick() {
    if (this.mode.equals("player")) {
      Vertex currentPlayerSquare = this.maze.vertices.get(player.posn.x).get(player.posn.y);
      currentPlayerSquare.color = new Color(50, 50, 170);
      currentPlayerSquare.visited = true;
    }
    else if (this.mode.equals("dfs")) {
      runDFS();
      if (maze.current != null && maze.current.equals(maze.vertices.get(0).get(0))) {
        this.maze.setDone();
      }
    }
    else if (this.mode.equals("bfs")) {
      runBFS();
      if (maze.current != null && maze.current.equals(maze.vertices.get(0).get(0))) {
        this.maze.setDone();
      }
    }
  }

  // run a depth first search for the goal
  void runDFS() {
    if (maze.workListDFS.size() > 0 && maze.current == null) {
      maze.dFS();
    }
    else if (maze.current != null && !maze.current.equals(maze.vertices.get(0).get(0))) {
      maze.reconstructDFS();
    }
  }

  // run a breadth first search for the goal
  void runBFS() {
    if (maze.workListBFS.size() > 0 && maze.current == null) {
      maze.bFS();
    }
    else if (maze.current != null && !maze.current.equals(maze.vertices.get(0).get(0))) {
      maze.reconstructBFS();
    }
  }

  // moves the Player when the player presses an arrow key
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      this.maze = new Maze(WIDTH, HEIGHT);
      this.player = new Player(0, 0);
      this.maze.dFSprep();
      this.maze.bFSprep();
    }
    else if (ke.equals("p")) {
      this.mode = "player";
      this.maze.reset();
      this.player = new Player(0, 0);
    }
    else if (ke.equals("d")) {
      this.mode = "dfs";
      this.maze.reset();
      this.maze.dFSprep();
    }
    else if (ke.equals("b")) {
      this.mode = "bfs";
      this.maze.reset();
      this.maze.bFSprep();
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
    else if (maze.isSolved()) {
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

  WorldImage getInstructionsImage1() {
    return new TextImage(
        "Press b for breadth first search      " + "Press d for depth first search   ",
        Color.BLACK);
  }

  WorldImage getInstructionsImage2() {
    return new TextImage(
        "Press p to solve the maze yourself       " + "Press r to generate a new maze",
        Color.BLACK);
  }
}

// the tests for the MazeWorld game
class ExamplesMaze {

  MazeWorld world = new MazeWorld();
  Maze maze = new Maze(10, 10);
  HashMap<Integer, Integer> reps;
  HashMap<Integer, Integer> reps2;

  // reset test conditions
  void reset() {
    world = new MazeWorld();
    world.maze = new Maze(MazeWorld.WIDTH, MazeWorld.HEIGHT, new Random(5));

    maze = new Maze(10, 10);

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

  // test getFinalEdges
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

  // test containedInOutEdges
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

  // test numTrees
  void testNumTrees(Tester t) {
    reset();
    t.checkExpect(maze.numTrees(reps), 100);
    reset();
    t.checkExpect(maze.numTrees(reps2), 0);
  }

  // test find
  void testFind(Tester t) {
    reset();
    t.checkExpect(maze.find(reps, 0), 0);
    t.checkExpect(maze.find(reps, 50), 50);
  }

  // test union
  void testUnion(Tester t) {
    reset();
    maze.union(reps, 0, 0);
    t.checkExpect(reps.get(0), 0);
    reset();
    maze.union(reps, 50, 0);
    t.checkExpect(reps.get(0), 50);
  }

  // test sortEdges
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

  // test getVerticalLine
  void testGetVerticalLine(Tester t) {
    Edge e = new Edge();
    t.checkExpect(e.getVerticalLine(), new LineImage(new Posn(0, Vertex.SIZE), Color.BLACK));
  }

  // test getHorizontalLine
  void testGetHorizontalLine(Tester t) {
    Edge e = new Edge();
    t.checkExpect(e.getHorizontalLine(), new LineImage(new Posn(Vertex.SIZE, 0), Color.BLACK));
  }

  // testEdgeEquals
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

  // test addEdges
  void testAddEdges(Tester t) {
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

  // test onKeyEvent
  void testOnKeyEvent(Tester t) {
    // player mode tests
    reset();
    t.checkExpect(world.mode, "player");

    Maze oldMaze = world.maze;
    world.onKeyEvent("r");
    t.checkExpect(world.mode, "player");
    t.checkFail(world.maze, oldMaze);

    // movement
    reset();
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.onKeyEvent("up");
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.onKeyEvent("left");
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.onKeyEvent("down");
    t.checkExpect(world.player.posn, new Posn(0, 1));

    world.onKeyEvent("right");
    t.checkExpect(world.player.posn, new Posn(1, 1));

    world.onKeyEvent("up");
    t.checkExpect(world.player.posn, new Posn(1, 0));

    world.onKeyEvent("right");
    t.checkExpect(world.player.posn, new Posn(1, 0));

    // dfs mode tests
    reset();
    t.checkExpect(world.mode, "player");

    world.onKeyEvent("d");
    t.checkExpect(world.mode, "dfs");

    oldMaze = world.maze;
    world.onKeyEvent("r");
    t.checkExpect(world.mode, "dfs");
    t.checkFail(world.maze, oldMaze);

    // bfs mode tests
    reset();
    t.checkExpect(world.mode, "player");

    world.onKeyEvent("b");
    t.checkExpect(world.mode, "bfs");

    oldMaze = world.maze;
    world.onKeyEvent("r");
    t.checkExpect(world.mode, "bfs");
    t.checkFail(world.maze, oldMaze);

    // invalid input
    reset();
    t.checkExpect(world.mode, "player");
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.onKeyEvent("a");
    world.onKeyEvent("c");
    world.onKeyEvent("!");
    world.onKeyEvent("m");

    t.checkExpect(world.mode, "player");
    t.checkExpect(world.player.posn, new Posn(0, 0));

    reset();
  }

  // test movePlayer
  void testMovePlayer(Tester t) {
    reset();
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.player.movePlayer("up", world.maze.walls);
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.player.movePlayer("left", world.maze.walls);
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.player.movePlayer("down", world.maze.walls);
    t.checkExpect(world.player.posn, new Posn(0, 1));

    world.player.movePlayer("right", world.maze.walls);
    t.checkExpect(world.player.posn, new Posn(1, 1));

    world.player.movePlayer("up", world.maze.walls);
    t.checkExpect(world.player.posn, new Posn(1, 0));

    world.player.movePlayer("right", world.maze.walls);
    t.checkExpect(world.player.posn, new Posn(1, 0));

    // invalid input
    reset();
    t.checkExpect(world.player.posn, new Posn(0, 0));

    world.player.movePlayer("a", world.maze.walls);
    world.player.movePlayer("c", world.maze.walls);
    world.player.movePlayer("!", world.maze.walls);
    world.player.movePlayer("m", world.maze.walls);

    t.checkExpect(world.player.posn, new Posn(0, 0));

    reset();
  }

  // run the game
  public static void main(String[] argv) {
    // run the game
    MazeWorld w = new MazeWorld();
    w.bigBang(MazeWorld.WINDOW_WIDTH, MazeWorld.WINDOW_HEIGHT + Vertex.SIZE * 2, 0.05);
  }
}
