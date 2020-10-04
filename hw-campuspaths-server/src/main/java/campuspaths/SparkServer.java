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

package campuspaths;


import campuspaths.utils.CORSFilter;

import spark.Spark;
import pathfinder.CampusMap;
import spark.Request;
import spark.Route;
import spark.Response;
import com.google.gson.Gson;
public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().
        CampusMap m = new CampusMap();

        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Gson g = new Gson();
                return  g.toJson(m.buildingNames());
            }
        });

        Spark.get("/path", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Gson g = new Gson();
                String from = request.queryParams("from"); // check if should be converted to upper case
                String to = request.queryParams("to"); // check if should be converted to upper case
                if(!m.shortNameExists(from) || !m.shortNameExists(from)) {
                    return "";
                }
                return g.toJson(m.findShortestPath(from,to));
            }
        });
    }
}
