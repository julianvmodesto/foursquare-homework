
# Route Matching

### How the Algorithm Works

First, we'll split the routes into static and dynamic routes.
This is done by checking wether `/X` exists in the route.

This is because when we iterate through the input paths that we 
want to match against, we'll first check against the static
routes, and then the dynamic routes because static routes will 
always override dynamic routes if there's a match between multiple.

To check against the static routes, we'll have the static routes
stored in a `Map<String, String>`, where the key is the static
route. If there exists a static route, then we don't check the
dynamic routes. If there isn't a static route, then there may
exist a dynamic route for the path.

In this case, we need to check the path against *all* dynamic
routes in case there is a match.

For the current path, we iterate over the dynamic routes.

```
for (Route dynamicRoute : dynamicRoutes) {
    ...

```

We split the current path and the current dynamic route by `/`.

First we check if these resulting parts are of the same length.
If so, then we have a possible match. For each component of
the path, we check that the component of the path matches the
corresponding component of the dynamic route, or that the
dynamic route is a wildcard `X`. If this is true for each 
component, then we have a route pattern match!

If not, then we have no dynamic route pattern match. Because we
checked static routes previously, we then return a `404`.

### Runtime Analysis


We'll assume the following:

```
l = the number of request paths
m = the number of routes
n = the number of components in the request path
```

What we want is for an algorithm that depends on `n`.

To start, we split the routes into dynamic and static routes,
which takes `O(m)` time.

Then, we have to iterate over all paths, which takes `O(l)`
time. We check against static routes in `O(1)` constant time,
then we move on and check against static routes.

Here, we split the path and the dynamic routes into components
by splitting on `/`. Then, we check each component of the path
against the corresponding component of the dynamic route in
`O(n)` time – this is what we want! An algorithm that is bound
by "the number of components in the request path".
