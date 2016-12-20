package q2;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Q2Test {
    @Test
    public void canRouteAll() throws Exception {
        List<Q2.Route> routes = Arrays.asList(
                new Q2.Route("/", "rootEndpoint"),
                new Q2.Route("/user", "userRootEndpoint"),
                new Q2.Route("/user/friends", "userFriendsEndpoint"),
                new Q2.Route("/user/lists", "userListsEndpoint"),
                new Q2.Route("/user/X", "userEndpoint"),
                new Q2.Route("/user/X/friends", "userFriendsEndpoint"),
                new Q2.Route("/user/X/lists", "userListsEndpoint"),
                new Q2.Route("/user/X/lists/X", "userListIdEndpoint"),
                new Q2.Route("/X/friends", "userFriendsEndpoint"),
                new Q2.Route("/X/lists", "userListsEndpoint"),
                new Q2.Route("/settings", "settingsEndpoint ")
        );
        List<String> paths = Arrays.asList(
                "/",
                "/user",
                "/user/friends",
                "/user/123",
                "/user/123/friends",
                "/user/123/friends/zzz",
                "/user/friends/friends",
                "/abc/lists",
                "/settings",
                "/aaa/bbb"
        );
        List<String> actual = Q2.routeAll(routes, paths);

        List<String> expected = Arrays.asList(
                "rootEndpoint",
                "userRootEndpoint",
                "userFriendsEndpoint",
                "userEndpoint",
                "userFriendsEndpoint",
                "404",
                "userFriendsEndpoint",
                "userListsEndpoint",
                "settingsEndpoint",
                "404"
        );
        assertEquals(actual, expected);
    }
}