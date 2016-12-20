package q2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q2 {

    static List<String> routeAll(List<Route> routes, List<String> paths) {
        List<String> endpoints = new ArrayList<String>();
        // Your code here

        Map<String, String> staticRoutes = new HashMap<>();
        List<Route> dynamicRoutes = new ArrayList<>();
        for (Route route : routes) {
            if (!route.path.contains(("/X"))) {
                staticRoutes.put(route.path, route.endpoint);
            } else {
                dynamicRoutes.add(route);
            }
        }

        System.out.println(staticRoutes.toString());

        for (String path : paths) {
            if (staticRoutes.containsKey(path)) {
                endpoints.add(staticRoutes.get(path));
            } else {
                boolean isFound = false;
                for (Route dynamicRoute : dynamicRoutes) {
                    // We have to check all dynamic routes
                    if (isFound) {
                        break;
                    }

                    String[] dynamicRoutePaths = dynamicRoute.path.split("/");
                    String[] pathParts = path.split("/");

                    if (dynamicRoutePaths.length == pathParts.length) {
                        for (int i = 0; i < dynamicRoutePaths.length; i++) {
                            if (!dynamicRoutePaths[i].equals(pathParts[i]) && !dynamicRoutePaths[i].equals("X")) {
                                break;
                            }
                            if (i == dynamicRoutePaths.length - 1) {
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
