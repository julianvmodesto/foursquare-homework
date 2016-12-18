package q1;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;


class Q1Test {
    @Test
    public void canGetUncoveredIntervals() throws Exception {
        List<Q1.Interval> input = Arrays.asList(
                new Q1.Interval(2, 6),
                new Q1.Interval(9, 12),
                new Q1.Interval(8, 9),
                new Q1.Interval(18, 21),
                new Q1.Interval(4, 7),
                new Q1.Interval(10, 11)
        );


        List<Q1.Interval> actual = Q1.uncoveredIntervals(input);

        assertThat(actual, IsCollectionContaining.hasItems(
                new Q1.Interval(7, 8),
                new Q1.Interval(12, 18)));
    }
}