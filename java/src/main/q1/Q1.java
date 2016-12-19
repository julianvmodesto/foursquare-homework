package q1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Q1 {
    static class Interval {
        int start;
        int end;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Interval interval = (Interval) o;

            if (start != interval.start) return false;
            return end == interval.end;
        }

        @Override
        public int hashCode() {
            int result = start;
            result = 31 * result + end;
            return result;
        }

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return start + " " + end;
        }
    }

    static List<Interval> uncoveredIntervals(List<Q1.Interval> intervals) {
        List<Interval> uncovered = new ArrayList<Q1.Interval>();

        // Your code here

        // O(log n)
        intervals.sort(Comparator.comparingInt(a -> a.end));

        // O(n)
        Map<Integer, Boolean> ends = new HashMap<>();
        for (Interval covered : intervals) {
            ends.put(covered.start, true);
        }

        // O(n)
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

        return uncovered;
    }

    /*
     *  Hey! You probably don't need to edit anything below here
     */

    private static List<Q1.Interval> readIntervals(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Q1.Interval> intervals = new ArrayList<Q1.Interval>();
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            intervals.add(toInterval(line));
        }
        return intervals;
    }

    private static Q1.Interval toInterval(String line) {
        final String[] tokenizedInterval = line.split(" ");

        return new Interval(Integer.valueOf(tokenizedInterval[0]),
                Integer.valueOf(tokenizedInterval[1]));
    }

    public static void main(String... args) throws IOException {
        List<Q1.Interval> intervals = Q1.readIntervals(System.in);
        List<Q1.Interval> uncovered = Q1.uncoveredIntervals(intervals);
        for (Interval i : uncovered) {
            System.out.println(i.start + " " + i.end);
        }
    }
}
