# Data-Communications-and-Networks
Project 1  
Spring 2016


Note: this code requires the Parallel Java 2 (PJ2) library in order to run.   
The classpath must be set accordingly before the code can be compiled.   
PJ2 and instructions for its use can be found at this URL: https://www.cs.rit.edu/~ark/pj2.shtml

Please use the following command lines to run the appropriate programs:

Command line: java pj2 MonteCarloPSmp seed V lowerP upperP T increment  
where   
seed = Random seed   
V = number of vertices   
lowerP = Lower bound of edge probability   
upperP = Upper bound of edge probability  
T = Number of trials  
increment = number by which to increment the knob   

Command line: java pj2 MonteCarloVSmp seed lowerV upperV p T increment     
where  
seed = Random seed   
lowerV = Lower bound of number of vertices   
upperV = Upper bound of number of vertices  
p = Edge probability  
T = Number of trials  
increment = number by which to increment the knob  
