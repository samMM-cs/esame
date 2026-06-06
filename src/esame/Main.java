package esame;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import esame.graph.Graph;

public class Main {
  static long start;
  static long end;

  public static void debug(String msg, boolean newline) {
    System.out.print(System.currentTimeMillis() + "\t" + msg + (newline ? "\n" : ""));
  }

  public static void debug(String msg) {
    debug(msg, true);
  }

  public static void main(String[] args) {
    if (args.length == 0)
      System.err.println("ERROR: no files provided");
    for (String arg : args) {
      try {
        start = System.currentTimeMillis();
        // debug("Parsing file " + arg);
        ProblemInput input = Parser.parseInputFile(arg);
        // debug("Parsed: " + input);
        // debug("Building graph");
        Graph graph = new Graph(input.V(), input.E(), input.edges());
        // debug("Built graph: " + graph);
        // debug("Finding first path");
        Paths paths = new Paths(input.inverseMapping(), graph, input.source(), input.stops(), input.destination());
        // debug("Found first path: ", false);
        printOutput(paths.steps(), paths.cost(), 0);
        for (int i = 0; i < input.updates().size(); i++) {
          // debug("Finding path with " + (i + 1) + " updates");
          graph.applyUpdate(input.updates().get(i));
          paths = new Paths(input.inverseMapping(), graph, input.source(), input.stops(), input.destination());
          // debug("Found path with " + (i + 1) + " updates", false);
          printOutput(paths.steps(), paths.cost(), i + 1);
        }
        end = System.currentTimeMillis();
        System.out.println("Solving " + arg + " took " + (end - start) + "ms in total");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void printOutput(List<String> steps, double cost, int updates) {
    // System.out.printf("Percorso ottimale con %d aggiornamenti\n", updates);
    // for (int i = 0; i < steps.size(); i++)
    // System.out.print(steps.get(i) + ((i < steps.size() - 1) ? " - " : ""));
    // System.out.println();
    System.out.printf(Locale.ROOT, updates + " Costo totale: %.2f\n", cost);
  }
}
