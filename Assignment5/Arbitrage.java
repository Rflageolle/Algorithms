/* This program will take
 *
 */

import java.util.*;
import java.text.*;
import java.io.*;

public class Arbitrage {
    // Helper interface for filtering
    public interface FILTER<T> { boolean keep(T it); }
    // Helper filter method
    public static <T> List<T> filter(List<T> source, FILTER<T> fil) {
        List<T> match = new ArrayList<T>();
        for(T it : source) {
            if(it != null && fil.keep(it)) { match.add(it); }
        }
        return match;
    }

    /* WorkData class
     * ===================================
     * Used to store location in the graph
     */
    public static class WorkData {
        public String currLocation;
        public String startLocation;
        public List<String> path;
        public double exchRate;
        public double score;
        @Override public String toString() {
            return "path: " + String.join(" > ", path) + " exchange rate: " + exchRate + " score: " + score;
        }
    }

    /* Transition Method
     * ===================================
     * Variables:
     *      currLocation: used as the current location in graph
     *
     *      startLocation: used to store starting location of traversal
     *
     *      exchRate: the exchange rate to go from currLocation to next
     *
     *      score: stores negative cycle
     *
     *      path: ArrayList of the steps taken through graph
     */
    public static WorkData step(WorkData data, String curr) {
        if(!data.currLocation.equals(curr)) {
            WorkData next = new WorkData();

            next.currLocation = curr;
            next.startLocation = data.startLocation;
            next.exchRate = data.exchRate * graph.get(data.currLocation).get(curr);
            next.score = (next.exchRate - 1.0) / data.path.size();
            next.path = new ArrayList<>(data.path);
            next.path.add(curr);

            return next;
        }
        return data;
    }

    // Helper to transition a node back to start
    public static WorkData stepToStart(WorkData data) {
        return step(data, data.startLocation);
    }

    // Graph of Vertex names
    private static final Map<String, Map<String, Double>> graph = new HashMap<>();

    // Queue of work to do
    private static final LinkedList<WorkData> queue = new LinkedList<>();

    // Profitable paths
    private static final List<WorkData> profitable = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Scanner file = new Scanner(new File("exchangeRates.txt"));

            while(file.hasNext()) {
                String line = file.nextLine();
                List<String> parts = filter(Arrays.asList(line.split("\t")), it -> !"".equals(it));
                if(parts.size() != 3) {
                    continue;
                }

                String from = parts.get(0);
                String to = parts.get(1);
                String rateString = parts.get(2);
                double rate = 0;

                // Skip any triplets without a number as the third thing
                try {
                    rate = Double.parseDouble(rateString);
                } catch(Exception e) {
                    continue;
                }

                if(!graph.containsKey(from)) {
                    graph.put(from, new HashMap<>());
                }
                if(!graph.containsKey(to)) {
                    graph.put(to, new HashMap<>());
                }

                // Record transition rates into graph
                graph.get(from).put(to, rate);
                graph.get(to).put(from, 1.0 / rate);
            }
            // File is read
            file.close();

            // Iterate graph and seed the queue with starting everywhere
            for(String key : graph.keySet()) {
                WorkData data = new WorkData();
                data.currLocation = key;
                data.startLocation = key;
                data.exchRate = 1.0;
                data.score = 0.0;
                data.path = Arrays.asList(key);

                queue.add(data);
            }

            while(queue.size() > 0) {
                WorkData current = queue.poll();
                WorkData compare = step(current, current.startLocation);

                for(String key : graph.keySet()) {
                    // Skip where we've already been
                    if(!current.path.contains(key)) {
                        WorkData next = step(current, key);
                        WorkData scored = step(next, next.startLocation);

                        if(scored.exchRate > 1) {
                            profitable.add(scored);
                        }
                        if(scored.exchRate >= compare.exchRate) {
                            queue.add(next);
                        }
                    }
                }
            }

            // Build file output
            StringBuilder strBuild = new StringBuilder();
            // sort ascending by rate
            Collections.sort(profitable, (a, b) -> Double.compare(a.exchRate, b.exchRate));

            strBuild.append("Found " + profitable.size() + " arbitrage opportunities\n");

            // Sort descending by rate
            Collections.sort(profitable, (a, b) -> Double.compare(b.exchRate, a.exchRate));
            strBuild.append("\nBest non-repeating path: " + profitable.get(0) + "\n");

            // Sort descending by score
            Collections.sort(profitable, (a, b) -> Double.compare(b.score, a.score));
            strBuild.append("\nMost effiecient path: " + profitable.get(0) + "\n\n");

            // Iterate profitable stuff and add to string builder
            strBuild.append("All Paths Found:\n");
            DecimalFormat df = new DecimalFormat("####.##");
            for(int i = 0; i < profitable.size(); i++) {
                WorkData data = profitable.get(i);
                strBuild.append("Path " + (i + 1) + ": " + String.join(" > ", data.path) + ", $1000.00 > $" + df.format(data.exchRate * 1000.0) + "\n");
            }

            PrintWriter out = new PrintWriter(new File("arbitrage_output.txt"));
            out.println(strBuild.toString());
            out.flush();
            out.close();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
