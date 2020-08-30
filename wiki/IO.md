# IO

## Command Line Interface
The Apache Commons CLI library was used to read the optional command line arguments. The first two arguments are read as String[] args. The Apache Commons CLI library help formatter is used to format and print the help statements for when a user inputs inappropriate arguments. Custom exceptions are also implemented to more accurately show where a user error has occurred.

## Input and Output Files
The Apache Commons Lang library was used to assist in the reading of the graph information in the dot files. We decided to implement our own dot reader as it appeared to be much simpler and more customisable than using an external tool. Custom exceptions were implemented for the incorrectly formated entry lines as the expected format is:
    
                                                               id  [Weight=int]  
                                                                           
Any other lines in the dot file are ignored. As for outputting the result to the dot files, this is also done through a custom writer with the digraph name is prepended with "output".
