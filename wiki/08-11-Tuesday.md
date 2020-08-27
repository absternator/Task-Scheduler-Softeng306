# Tuesday 11 AUG
###### Suggestion from Anmol:
The set of scheduled tasks in PartialSolution would take a large amount of memory. Since the PartialSolution has reference to its parent node, then it could just store the latest scheduled task, and refer to its parents for the rest of the scheduled tasks.
