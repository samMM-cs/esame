package esame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import esame.graph.Edge;

public class Parser {
  private static String readNonEmptyLine(BufferedReader br) throws IOException {
    String line;
    while ((line = br.readLine()) != null) {
      if (!line.trim().isEmpty()) {
        System.out.println(line);
        return line;
      }
    }
    return null;
  }

  public static ProblemInput parseInputFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      Map<String, Integer> cityMap = new HashMap<>();

      // read problem sizes from the file
      String line = readNonEmptyLine(br);
      String[] VE = line.trim().split("\\s+");
      int V = Integer.parseInt(VE[0]);
      int E = Integer.parseInt(VE[1]);

      // read edges from the files
      List<Edge> edges = new ArrayList<>();
      for (int i = 0; i < E; i++) {
        line = readNonEmptyLine(br);
        String[] parts = line.trim().split("\\s+");
        String u = parts[0];
        String v = parts[1];
        int weight = Integer.parseInt(parts[2]);
        cityMap.putIfAbsent(u, cityMap.size());
        cityMap.putIfAbsent(v, cityMap.size());
        edges.add(new Edge(cityMap.get(u), cityMap.get(v), weight));
      }

      // check if V cities were in the original routes
      if (cityMap.size() < V)
        throw new IOException("Too few cities in the original routes");
      if (cityMap.size() > V)
        throw new IOException("Too many cities in the original routes");

      line = readNonEmptyLine(br);
      if (!line.trim().startsWith("PARTENZA"))
        throw new IOException("PARTENZA mancante");
      Integer partenza = cityMap.get(line.trim().split("\\s+", 2)[1].trim());
      if (partenza == null)
        throw new IOException("PARTENZA mancante");

      line = readNonEmptyLine(br);
      if (!line.trim().startsWith("DESTINAZIONE"))
        throw new IOException("DESTINAZIONE mancante");
      Integer destinazione = cityMap.get(line.trim().split("\\s+", 2)[1].trim());
      if (destinazione == null)
        throw new IOException("DESTINAZIONE mancante");

      line = readNonEmptyLine(br);
      if (!line.trim().startsWith("TAPPE"))
        throw new IOException("TAPPE mancanti");
      List<Integer> tappe = new ArrayList<>();
      while ((line = readNonEmptyLine(br)) != null && !line.trim().equals("AGGIORNAMENTI")) {
        Integer tappa = cityMap.get(line.trim());
        if (tappa == null)
          throw new IOException("Tappa mancante: " + line);
        tappe.add(tappa);
      }

      List<Edge> aggiornamenti = new ArrayList<>();
      if (line != null) {
        while ((line = readNonEmptyLine(br)) != null) {
          String[] parts = line.trim().split("\\s+");
          String u = parts[0];
          String v = parts[1];
          int weight = Integer.parseInt(parts[2]);
          cityMap.putIfAbsent(u, cityMap.size());
          cityMap.putIfAbsent(v, cityMap.size());
          edges.add(new Edge(cityMap.get(u), cityMap.get(v), weight));
        }
      }
      return new ProblemInput(V, E, edges, partenza, destinazione, tappe, aggiornamenti, cityMap);
    }
  }
}
