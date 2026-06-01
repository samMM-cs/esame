package esame;

import java.io.IOException;

import esame.graph.Graph;

public class Main {
  public static void main(String[] args) {
    if (args.length == 0)
      System.err.println("ERROR: no files provided");

    for (String arg : args) {
      try {
        ProblemInput input = Parser.parseInputFile(arg);
        Graph graph = new Graph(input.V(), input.E(), input.edges());
        System.out.println(graph);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
