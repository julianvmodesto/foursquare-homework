import sys

class Interval(object):
    def __init__(self, start, end):
        self.start = start
        self.end = end

def uncovered_intervals(intervals):
    uncovered = []
    #
    # Your code here
    #
    return uncovered

def main():
    intervals = []
    for line in sys.stdin.readlines():
        if not line.strip():
            continue
        start, _, end = line.partition(' ')
        intervals.append(Interval(start, end))
    for interval in uncovered_intervals(intervals):
        print interval.start, interval.end

if __name__ == '__main__':
  main()