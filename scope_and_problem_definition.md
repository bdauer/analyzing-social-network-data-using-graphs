## Overview

How do videos spread on Twitter? I'll use a heuristic to identify different types of influence and different likelihoods of sharing a video. Then I'll add functionality that allows a particular user to be selected as the first to introduce a video.



## Data

The provided UCSD twitter data



## Questions

**Easier**: Identify trend setters and trend followers.

Trend setters have more followers than most of their followers. They're more likely to share videos that have been viewed fewer times, and that fewer of the people they follow have shared. They don't all really set trends, but they think they do.

Trend followers don't have more followers than most of their followers. They're more likely to share videos that have been shared more times, and videos that have a high rating.



**Harder**: Assuming the starting point of a provided user, how many people does a video reach? What does the shape of the new graph look like?



## Algorithms and Data Structures

I've have a classic graph using an adjacency list. Vertices will represent individuals. Edges will represent relationships.

**Easier Question**: I'll need to count the number of out-edges for every node. That number, the number of of followers, will be stored in the node for later use.

Then I'll need to compare each node's number of edges against the number of edges of each of its neighbors. If it's higher than a good majority of its neighbors (maybe 80%) then it's a trend setter. Otherwise it's a trend follower.

**Harder Question**: For the harder question I'm tackling a type of network cascade. I'll be taking into account values tied to the video itself (number of views, rating), but also values tied to in-neighbors (number of them who have already shared the video). Because of the latter I think I'll want to keep a list of each node's in-neighbors within the node. That way I can iterate over a list instead of searching the graph each time.

I'll need to accept a node number identifying the first person in a network to share a video. I'll also need to accept an initial number of views and an initial rating. If I have time I may overload the method so that if arguments are missing, defaults are provided.


```
For each node,

    if the video was already shared to the node:
        then it's not shared.

    Mark the node as having viewed the video.


    if the node represents a trend setter:

        If the video has not been shared by more than a minimum
        numbers of in-neighbors (maybe less than 20%)
        and the numbers of views is not too high:

(I'll need to determine the threshold, maybe based on the size of the network, or the size of a user's egonet up to a number of views that's always considered a lot)

            then the video gets shared to all out-nodes.

        if the node represents a trend follower:

            if the number of views is high
            or the rating is high:
                then the video gets shared to all out-nodes.

        if the video was shared:
            increase the number of viewers by 1 + the number out-nodes.
            increase the rating (need to determine how to do so).

        if the video was not shared and the node had not previously viewed the video:
            reduce the rating.
```

I'm sure I'll need to tweak this algorithm a bit as I see it in action and find places where I've overlooked something.

I might create a separate video class to be passed. This way I can keep the method overloading in its constructor and have a uniform API for the algorithm.



## Algorithm Analysis, Limitations, Risk:

**Easier question**: For the first part the runtime should be linear. I can include a function that returns the length of the out-neighbors list in my nodes. I'll need to visit every node once O(|V|) plus however many times it's a follower. In a worst case scenario I'm looking at O(|V||V|), but since most people don't follow everyone else, it should be much faster than that.

**Harder question**: When I construct the graph I'll need to store in-neighbors for each node. That will only change the initial construction linearly since I can build those connections at the same time as I'm building the out-neighbors.

At that point the time looks similar to the easier question. The difference is that instead of comparing to out-neighbors, I'll be comparing to in-neighbors.

**Other Challenges**:

Once a graph has been built, I'll probably need to look into how to store the data so that I'm not building it over and over again. I've saved data in c and python, so I'm hoping it will be pretty straightforward. I may also want to look into Java libraries for graph visualization or alternatively, into libraries for visualizing graphs in Python. I'm not entirely sure how well my heuristic will work for determining who is a trend setter vs follower and I'm pretty sure the way I'm determining the likelihood of sharing is too simple.
