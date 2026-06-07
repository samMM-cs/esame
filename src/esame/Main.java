package esame;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import esame.graph.Graph;

public class Main {
  public static void main(String[] args) {
    if (args.length == 0)
      System.err.println("ERROR: no files provided");
    for (String arg : args) {
      try {
        long start = System.currentTimeMillis();
        ProblemInput input = Parser.parseInputFile(arg);
        Graph graph = new Graph(input.V(), input.E(), input.edges());
        Paths paths = new Paths(input.inverseMapping(), graph, input.source(), input.stops(), input.destination());
        printOutput(paths.steps(), paths.cost(), 0);
        for (int i = 0; i < input.updates().size(); i++) {
          graph.applyUpdate(input.updates().get(i));
          paths = new Paths(input.inverseMapping(), graph, input.source(), input.stops(), input.destination());
          printOutput(paths.steps(), paths.cost(), i + 1);
        }
        long end = System.currentTimeMillis();
        System.out.println("Solving " + arg + " took " + (end - start) + "ms in total");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void printOutput(List<String> steps, double cost, int updates) {
    System.out.printf("Percorso ottimale con %d aggiornamenti\n", updates);
    for (int i = 0; i < steps.size(); i++)
      System.out.print(steps.get(i) + ((i < steps.size() - 1) ? " - " : ""));
    System.out.println();
    System.out.printf(Locale.ROOT, "Costo totale: %.2f\n", cost);
  }
}
