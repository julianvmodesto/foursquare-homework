import sys

def main(args):
  # Generate the routes from the config
  routes = parse_config(args[0])
  # Get the paths we are trying to route
  paths = sys.stdin.readlines()

  #
  #   Your code here.
  #

  for path in paths:
    # Your code here
    # endpoint_or_404 = ...
    # print endpoint_or_404
    print '404' # deleteme

def parse_config(path):
    ''' Parse the config file and parse the mapping from routes to
        endpoints. The format is:

        / rootEndpoint
        /user/X userEndpoint
        /settings settingsEndpoint
        ...
    '''
    routes = []
    with open(path) as config_file:
        lines  = config_file.readlines()
    for line in lines:
        path, _, endpoint = line.partition(' ')
        routes.append((path, endpoint))
    return routes


if __name__ == '__main__':
  if len(sys.argv) != 2:
    print 'Usage: q2.py CONFIG_FILE < URLS_TO_ROUTE'
  main(sys.argv[1:])
