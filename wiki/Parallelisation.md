# Parallelisation

The depth first search algorithm is parallelised. The number of threads created is according to the number of cores minus one, with the main thread also used to expand the solution. The threads are continually reused to expand the partial solutions. They do this by being in a while loop and breaks out of the while loop when open is detected to be empty. The open stack can only be accessed through two synchronized methods and is thus thread safe.

Libraries were not used to parallelise the algorithm as the initial implementation did yield better performance so there was little incentive to investigate alternative implementations.