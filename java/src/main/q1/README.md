
# Uncovered Intervals

### How the Algorithm Works

Let's start with the following covered intervals.

```
2 6
9 12
8 9
18 21
4 7
10 11
```

We expect the following uncovered intervals as our output.

```
7 8
12 18
```

First, we sort the covered intervals by their ends.

```
2 6
4 7
8 9
10 11
9 12
18 21
```

By stepping through the list of covered intervals and comparing
the previous interval end with the next interval start, we can
see that there's a pattern. If the previous interval end is
less than the next interval start, then we've found an uncovered
interval. But only if the *previous interval end* is not an
interval start of any other intervals.

For example, we take our first comparison with (2, 6) and (4, 7).
The previous interval end is greater than the next interval start
i.e. `6 > 4`, so we continue. We take the comparison (4, 7) and
(8, 9). Here, the previous interval end is less than the next
interval start i.e. `7 < 8`. We check that the *previous interval
end* is not an interval start of any other intervals i.e. 7 is
not an interval start of any of the other intervals! So we've
found our first uncovered interval, (7, 8).

How can we verify this? Well by sorting the covered intervals by
interval end, then we can say that the previous interval and all
intervals before can only cover up to the previous interval end,
exclusively. For example, with (9, 12) as our previous interval
and (18, 21) as our next interval, the previous interval and all 
intervals before can only possibly cover up to 12 because we've
sorted by the ends. Now, we looking at the next interval,
(18, 21). The next interval can only possibly cover up to 21, but
we now know that the previous interval can only cover up to 12.
Now looking at the next interval start, 18, we know for sure that
the interval from (12, 18) is uncovered because the covered 
intervals including and before (9, 12) can only cover up to 12.

### Runtime Analysis

We'll take `n` to be the number of covered intervals given.

```
intervals.sort(Comparator.comparingInt(a -> a.end));
```

First, we sort the covered intervals. Sorting algorithms have a 
Big O of `O(n*log(n))`.

```
Map<Integer, Boolean> ends = new HashMap<>();
for (Interval covered : intervals) {
    ends.put(covered.start, true);
}
```

Next, we step through the covered intervals and hash them in a
lookup table. This is done for `O(n)`, and provides `O(1)`
lookup, which is "free" if you compare the `O(n)` to our sort's
`O(n*log(n))`.

```
boolean processedFirst = false;
int lastEnd = -1;
for (Interval current : intervals) {
    if (processedFirst) {
        if (lastEnd < current.start) {
            if (!ends.containsKey(lastEnd)) {
                uncovered.add(new Interval(lastEnd, current.start));
            }
        }
    }
    processedFirst = true;
    lastEnd = current.end;
}
```

Finally, we step through the covered intervals one last time,
in `O(n)` time. We keep track of and compare the previous
interval end to the next interval start, and check do three
condition checks, which take constant time, to find any
possible uncovered interval. After stepping through all
covered intervals, we've found all uncovered intervals.

This algorithm is thus bound by our sorting of `O(n*log(n))`,
since the hashing and the last step take `O(n)` time.
