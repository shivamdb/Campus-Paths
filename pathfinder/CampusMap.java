/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder;

import graph.DirectedGraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

/**
 * A class represents a map of buildings on Campus
 */
public class CampusMap implements ModelAPI {
    // Representation Invariant:
    //  buildings!=null & graph!=null
    // no element of building an graph is null

    // Abstraction Function
    // AF(this)  =  a map of campus buildings
    // this represents an object made up of a graph of campus buildings
    // and also has a list of names of campus buildings
    /**
     * List of buildings
     */
    private List<CampusBuilding> buildings;
    /**
     * The graph which stores the nodes and edges
     */
    private DirectedGraph<Point, Double> graph;

    /**
     * makes a graph from the 2 tvs files given campus_buildings.tsv and
     * campus_paths.tsv
     */
    public CampusMap() {
        graph = new DirectedGraph<>();
        buildings = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths("campus_paths.tsv");
        Set<Point> points = new HashSet<>();
        for(CampusPath p : paths) {
            Point p1 = new Point(p.getX1(),p.getY1());
            Point p2 = new Point(p.getX2(),p.getY2());
            if(!points.contains(p2)) {
                graph.addNode(p2);
                points.add(p2);
            }
            if(!points.contains(p1)) {
                graph.addNode(p1);
                points.add(p1);
            }
            graph.addEdge(p1,p2,p.getDistance());
            graph.addEdge(p2, p1,p.getDistance());
        }
        checkRep();

    }
    /**
     *
     * @param shortName The short name of a building to query.
     * @return returns true if shortName is inside the campus map
     *          else false
     * @spec.requires shortName!=null
     */
    @Override
    public boolean shortNameExists(String shortName) {
        checkRep();
        for(CampusBuilding x: buildings) {
            if(x.getShortName().equals(shortName)) {
                checkRep();
                return true;
            }
        }
        checkRep();
        return false;
    }

    /**
     * the long name of the building which has the given shortName, if shortName valid
     * @param shortName The short name of a building to look up.
     * @return the long name of the building which has the given shortName is shortName valid
     * @throws IllegalArgumentException if shortName provided does not exist
     * @spec.requires shortName!=null
     */
    @Override
    public String longNameForShort(String shortName) {
        checkRep();
        for(CampusBuilding x: buildings) {
            if(x.getShortName().equals(shortName)) {
                checkRep();
                return x.getLongName();
            }
        }
        checkRep();
        throw new IllegalArgumentException();
    }

    /**
     *
     * @return A mapping from all the buildings' short names to their long names in this campus map.
     */
    @Override
    public Map<String, String> buildingNames() {
        checkRep();
        Map<String, String> m = new HashMap<>();
        for(CampusBuilding x: buildings) {
            if(!m.containsKey(x.getShortName())) {
                m.put(x.getShortName(), x.getLongName());
            }
        }
        checkRep();
        return m;
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        checkRep();
        Map<String, String> mapOfBuildings = buildingNames();
        if(!mapOfBuildings.containsKey(startShortName) || !mapOfBuildings.containsKey(endShortName)) {
            throw new IllegalArgumentException();
        }

        Point origin = null;
        Point destination = null;
        for(CampusBuilding x : buildings) {
            if(x.getShortName().equals(startShortName)) {
                origin = new Point(x.getX(),x.getY());
            }
            if(x.getShortName().equals(endShortName)) {
                destination = new Point(x.getX(),x.getY());
            }
        }

        if(origin == null || destination == null) {
            throw new IllegalArgumentException();
        }

        ShortestPath<Point>  path = new ShortestPath<>();
        checkRep();
        return path.findShortestPath(graph,origin, destination);
    }

    /**
     * checks if the representation invariant is not being violated
     * throws Error if the rep Invariant is being violated
     */
    private void checkRep() {
        assert graph !=null;
        assert buildings!=null;
    }
}
