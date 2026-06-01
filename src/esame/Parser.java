package esame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import esame.graph.Edge;

public class Parser {
  private static boolean isValidInput(ProblemInput input) {
    Set<String> cities = new HashSet<>();
    for (Edge<String> edge : input.edges()) {
      cities.add(edge.from());
      cities.add(edge.to());
    }
    for (Edge<String> edge : input.aggiornamenti()) {
      cities.add(edge.from());
      cities.add(edge.to());
    }

    return cities.size() == input.V() && input.edges().size() == input.E() && cities.contains(input.partenza())
        && cities.contains(input.destinazione()) && cities.containsAll(input.tappe());
  }

  private static String readNonEmptyLine(BufferedReader br) throws IOException {
    String line;
    while ((line = br.readLine()) != null) {
      if (!line.trim().isEmpty()) {
        return line;
      }
    }
    return null;
  }

  public static ProblemInput parseInputFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String line = readNonEmptyLine(br);
      String[] VE = line.trim().split("\\s+");
      int V = Integer.parseInt(VE[0]);
      int E = Integer.parseInt(VE[1]);

      List<Edge<String>> edges = new ArrayList<>();
      for (int i = 0; i < E; i++) {
        line = readNonEmptyLine(br);
        String[] parts = line.trim().split("\\s+");
        String u = parts[0];
        String v = parts[1];
        int weight = Integer.parseInt(parts[2]);
        edges.add(new Edge<>(u, v, weight));
      }

      line = readNonEmptyLine(br);
      if (!line.trim().startsWith("PARTENZA"))
        throw new IOException("PARTENZA mancante");
      String partenza = line.trim().split("\\s+", 2)[1].trim();

      line = readNonEmptyLine(br);
      if (!line.trim().startsWith("DESTINAZIONE"))
        throw new IOException("DESTINAZIONE mancante");
      String destinazione = line.trim().split("\\s+", 2)[1].trim();

      line = readNonEmptyLine(br);
      if (!line.trim().startsWith("TAPPE"))
        throw new IOException("TAPPE mancanti");
      List<String> tappe = new ArrayList<>();
      while ((line = readNonEmptyLine(br)) != null && !line.trim().equals("AGGIORNAMENTI"))
        tappe.add(line.trim());

      List<Edge<String>> aggiornamenti = new ArrayList<>();
      if (line != null) {
        while ((line = readNonEmptyLine(br)) != null) {
          String[] parts = line.trim().split("\\s+");
          String u = parts[0];
          String v = parts[1];
          int weight = Integer.parseInt(parts[2]);
          aggiornamenti.add(new Edge<>(u, v, weight));
        }
      }
      ProblemInput input = new ProblemInput(V, E, edges, partenza, destinazione, tappe, aggiornamenti);
      if (!isValidInput(input)) {
        throw new IOException("Malformed input file");
      }
      return input;
    }
  }
}
