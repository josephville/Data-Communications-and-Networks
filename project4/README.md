Data Communications and Networks  
Project 4

Note: this code requires the Parallel Java 2 (PJ2) library in order to run.   
The the classpath must be set accordingly before the code can be compiled.  
PJ2 and instructions for its use can be found at this URL: https://www.cs.rit.edu/~ark/pj2.shtml   

Please use the following command lines in order to run these programs:   

java PSweepDiscEventSim seed V k lowerP upperP pIncrement lambda mu N t transcript   
<seed = seed for graph generation (must not be a decimal number)  
V = number of vertices (must be an integer)  
k = parameter to determine graph density (must be an integer)  
lowerP = bound of p = rewiring probability  
upperP = upper bound of p = rewiring probability   
pIncrement = the value by which to increment p   
lambda = mean query arrival rate   
mu = mean query service rate   
N = number of queries to create (must be an integer)   
t = number of trials (must be an integer)   
transcript (optional) = a boolean whether to print the transcript. false if omitted.   

java KSweepDiscEventSim seed V lowerK upperK p lambda mu N t transcript   
seed = seed for graph generation (must not be a decimal number)   
V = number of vertices (must be an integer)   
lowerK = lower k parameter (an integer >= 1) to determine graph density   
upperK = upper k parameter to determine graph density   
p = rewiring probability; determines graph entropy   
lambda = mean query arrival rate   
mu = mean query service rate   
N = number of queries to create (must be an integer)   
t = number of trials (must be an integer)   
transcript (optional) = a boolean whether to print the transcript. false if omitted.   
