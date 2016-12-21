package q2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q2 {

    // l = the number of request paths
    // m = the number of routes
    // n = the number of components in the request path
    static List<String> routeAll(List<Route> routes, List<String> paths) {
        List<String> endpoints = new ArrayList<String>();

        // Split the routes into dynamic and static
        Map<String, String> staticRoutes = new HashMap<>();
        List<Route> dynamicRoutes = new ArrayList<>();
        // O(m)
        for (Route route : routes) {
            if (!route.path.contains(("/X"))) {
                staticRoutes.put(route.path, route.endpoint);
            } else {
                dynamicRoutes.add(route);
            }
        }

        // O(l)
        for (String path : paths) {
            // Check the static routes first in constant time
            // O(1)
            if (staticRoutes.containsKey(path)) {
                endpoints.add(staticRoutes.get(path));
            } else {
                boolean isFound = false;
                // If we couldn't find a matching static route, then check the dynamic routes
                // Worst case is all routes are dynamic
                // O(m)
                for (Route dynamicRoute : dynamicRoutes) {
                    // We have to check all dynamic routes unless we found one that matches
                    if (isFound) {
                        break;
                    }

                    // n = the number of components in the request path
                    String[] pathParts = path.split("/");
                    String[] dynamicRoutePaths = dynamicRoute.path.split("/");

                    // If the number of components in the request path != the number of components in the dynamic route,
                    // then this dynamic route isn't a match :(
                    if (pathParts.length == dynamicRoutePaths.length) {
                        // Step through the component in the request path and check that each matches the corresponding
                        // component in the dynamic route path, or that the component in the dynamic route path is a
                        // wildcard X
                        // O(n)
                        for (int i = 0; i < pathParts.length; i++) {
                            if (!pathParts[i].equals(dynamicRoutePaths[i]) && !dynamicRoutePaths[i].equals("X")) {
                                break;
                            }
                            if (i == pathParts.length - 1) {
                                isFound = true;
                                endpoints.add(dynamicRoute.endpoint);
                            }
                        }
                    }
                }
                if (!isFound) {
                    endpoints.add("404");
                }
            }
        }

        return endpoints;
    }

    /**
     *      Hey! You probably won't need to edit anything below here.
     */

    static class Route {
        String path;
        String endpoint;
        public Route(String path, String endpoint) {
            this.path = path;
            this.endpoint = endpoint;
        }
    }

    private static List<Route> getRoutes(InputStream is) throws IOException {
        List<Route> routes = new ArrayList<Route>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            String[] tokenizedLine = line.split(" ");
            routes.add(new Q2.Route(tokenizedLine[0], tokenizedLine[1]));
        }
        return routes;
    }

    private static List<String> getPaths(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> paths = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            paths.add(line);
        }
        return paths;
    }

    public static void main(String... args) throws IOException {
        List<Route> routes = Q2.getRoutes(new FileInputStream(args[0]));
        List<String> paths = Q2.getPaths(System.in);

        for(String endpoint : Q2.routeAll(routes, paths)) {
            System.out.println(endpoint);
        }
    }
}
