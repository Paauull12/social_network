package org.example.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {
    private final HashMap<Long, List<Long>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addNode(Long vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(Long vertex1, Long vertex2) {
        if (adjacencyList.containsKey(vertex1) && adjacencyList.containsKey(vertex2)) {
            if (!adjacencyList.get(vertex1).contains(vertex2)) {
                adjacencyList.get(vertex1).add(vertex2);
            }
            if (!adjacencyList.get(vertex2).contains(vertex1)) {
                adjacencyList.get(vertex2).add(vertex1);
            }
        }
    }

    public List<Long> getAdjVertices(Long vertex) {
        return adjacencyList.get(vertex);
    }

    private Long[] dfsFarthest(Long vertex, Set<Long> visited, Long distance) {
        visited.add(vertex);
        Long farthestNode = vertex;
        Long maxDistance = distance;

        for (Long neighbor : adjacencyList.get(vertex)) {
            if (!visited.contains(neighbor)) {
                Long[] result = dfsFarthest(neighbor, visited, distance + 1);
                if (result[1] > maxDistance) {
                    farthestNode = result[0];
                    maxDistance = result[1];
                }
            }
        }
        return new Long[]{farthestNode, maxDistance};
    }

    private Long findLongestPathInComponent(Long startNode) {
        Set<Long> visited = new HashSet<>();
        Long[] farthestFromStart = dfsFarthest(startNode, visited, 0L);
        Long farthestNode = farthestFromStart[0];

        visited.clear();
        Long[] longestPathResult = dfsFarthest(farthestNode, visited, 0L);

        return longestPathResult[1];
    }

    public Long[] countComponentsAndGetLongestRoad() {
        Set<Long> visited = new HashSet<>();
        Long connectedComponents = 0L;
        Long longestRoad = 0L;
        Long componentNode = -1L;

        for (Long vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex)) {
                connectedComponents++;

                Long currentLongestPath = findLongestPathInComponent(vertex);

                if (currentLongestPath > longestRoad) {
                    longestRoad = currentLongestPath;
                    componentNode = vertex;
                }

                dfsMarkVisited(vertex, visited);
            }
        }

        return new Long[]{connectedComponents, longestRoad, componentNode};
    }

    private void dfsMarkVisited(Long vertex, Set<Long> visited) {
        visited.add(vertex);
        for (Long neighbor : adjacencyList.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsMarkVisited(neighbor, visited);
            }
        }
    }
    
    public Set<Long> getAllFriendsExtendedToo(Long node){
        Set<Long> visited = new HashSet<>();
        
        Long[] smth = dfsFarthest(node, visited, 0L);
        
        return visited;
    }

    public void printGraph() {
        for (Long vertex : adjacencyList.keySet()) {
            System.out.println("Node " + vertex + ": " + adjacencyList.get(vertex));
        }
    }
}