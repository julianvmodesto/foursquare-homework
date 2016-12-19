Foursquare Engineering Take-Home Exercise

Hi! We’re excited that you’re interested in coming to work with us. Below you’ll
find a few exercises that will help us get acquainted with your skills and
strengths. We expect the exercises to take between three and four hours to
complete.

You may use any programming language you like, but if you choose to use Java or
Python we've included a zip file with scaffolding to save you some time.

Please submit your source files and design text file in a regular zip or tar.gz
file to me at cliff@foursquare.com by [12/20/2016]. You may also direct any
clarification or process questions to this address.

Feel free to make use of standard libraries and internet references in your
solutions, but all work must be your own. And finally, please do not enter your
name or other self-identifiers in any of your files.

Good luck!

Question One

Estimated time: 45-60 minutes

One of our monitoring tools records how long various operations within a request
take. A problem that we have is that sometimes parts are untimed. We'd like the
parts which are not covered by the intervals so that we can fill them in.

Write a program that reads a list of possibly-overlapping intervals from STDIN,
one per line, and outputs a list of the intervals not covered by the input
intervals. For example, given input

2 6 9 12 8 9 18 21 4 7 10 11

your program should output

7 8 12 18

You may use any programming language you’d like. We will test your program for
correctness, but we will also consider its design, efficiency, and readability.
You should test the code well enough that you think it's correct, but you don't
need to include any tests in your submission. You may also assume that the input
is well-formed (e.g. there are always two numbers per line, the second is always
larger than the first).

Please include a brief description of how your algorithm works, a discussion of
any trade-offs or assumptions you made in your design, and an analysis of its
running time (hint: you can do better than O(n2), where n is the number of input
lines). Please do not include any debugging output.

Question Two

Estimated time: 90-120 minutes

Many popular web frameworks route incoming requests to endpoints by matching the
request path against a series of regular expressions. The first regular
expression that matches determines which method should handle the request.
However, this becomes noticeably slow if your application needs to dispatch
among dozens or hundreds of endpoints.

Your task is to write a dispatch routine whose running time does not depend
linearly on the number of endpoints, but rather only on the number of components
in the request path.

Your program should read its configuration from a file whose name is given as
the first argument on the command line. This file will contain a list of path
patterns, one per line, each followed by a space and a string token (taken to
represent the name of the method that handles that endpoint). Path patterns may
contain wildcards, denoted by an X. Here is a sample configuration file:

/ rootEndpoint /user userRootEndpoint /user/friends userFriendsEndpoint
/user/lists userListsEndpoint /user/X userEndpoint /user/X/friends
userFriendsEndpoint /user/X/lists userListsEndpoint /user/X/lists/X
userListIdEndpoint /X/friends userFriendsEndpoint /X/lists userListsEndpoint
/settings settingsEndpoint

The program should read request paths from STDIN, one per line, and should write
the name of the method corresponding to the matching pattern to STDOUT. If
multiple path patterns match, you should prefer static patterns to patterns with
wildcards. If no patterns match, you should print the string “404”. Please do
not include any error or debugging output.

For example, given the above configuration and the following input

/ /user /user/friends /user/123 /user/123/friends /user/123/friends/zzz
/user/friends/friends /abc/lists /settings /aaa/bbb

your program should output

rootEndpoint userRootEndpoint userFriendsEndpoint userEndpoint
userFriendsEndpoint 404 userFriendsEndpoint userListsEndpoint settingsEndpoint
404

In other words, if the configuration file is called config.txt and the sample
input is in a file called in.txt, then invoking

$ your_program config.txt < in.txt

should produce the output above.

You can assume that path patterns will be delimited by the / character and that
wildcards will always appear by themselves (i.e., you won’t see /foo/barX/baz).

You may use any programming language you’d like. We will test your program for
correctness, but we will also consider its design, efficiency, and readability.
You should test the code well enough that you think it's correct, but you don't
need to include any tests in your submission. You may also assume that the input
is well-formed.

Please include a brief description of how your algorithm works, a discussion of
any trade-offs or assumptions you made in your design, and an analysis of its
time and space complexity.


Question Three

Estimated time: 45-60 minutes

An important aspect of being a software engineer is creating design documents
for large projects. A design document is a way to solidify your thoughts and
  allow your co-workers to understand the project. It also allows them to make
  helpful comments to improve the final design. Design docs are rarely perfect,
  and this one's purpose is to serve as a solid starting point for a discussion
  if you come into our offices for an interview.

Write a one-page design doc describing a service that keeps track of Foursquare
users as they check in at different venues. In particular, this service will get
informed of each check-in in real time (a user/venue pair) and must be able to
answer the following queries in real time:

Where is user A right now?  What users are at venue X right now?

The following business rules apply:

A user can only be at one venue at a time. If user A checks in at venue X and
then at venue Y, they are no longer at venue X.  A check-in only “lasts” for at
most 3 hours. If user A checks in at venue X and then does nothing for 3 hours,
they are no longer at venue X.

The service should have durable enough storage that you can restart it without
losing all of your data It should not store everything in memory.

Please be specific in your design. The most important aspect of the design is
that a reader can understand how they would implement this service if you
weren't around to ask questions to. The design should call out exactly how the
service implements each of the business rules above.

If you have time left after writing up the functional part of the design try to
include a few sentences on each of the following (if appropriate):

Time/space tradeoffs — are you pre-calculating or caching anything to save time
later, or recalculating values to save space?  Storage — If you're using a
database, describe what kind (sql, mongo, etc), how the data will be organized,
and any indexes. If storage is being spread out over multiple databases (e.g.
via sharding), how is that done?  Scalability / Availability — If you are going
to have multiple servers in your service, describe how you will distribute work
between them. What would happen if you suddenly had 100x more requests? What
would occur if one or more servers failed (in whatever way they could fail)?







