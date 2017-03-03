{\rtf1\ansi\ansicpg1252\deff0\nouicompat\deflang1033{\fonttbl{\f0\fnil\fcharset0 Calibri;}{\f1\fnil Calibri;}}
{\*\generator Riched20 10.0.14393}\viewkind4\uc1 
\pard\sa200\sl276\slmult1\f0\fs22\lang9 Programs:\par
Write a good sequential program for this problem. Look speci\f1\u-1279?cally for optimizations that you can apply to the sequential program to improve its performance. Include a description of these optimizations in your report. Develop a parallel solution for the problem from your sequential solution. You will need an ef\u-1279?cient barrier to synchronize your workers. Your solution should work for any number of workers from 2 through 32. In your report, include a discussion of the barrier that you used and optimizations that you applied to the parallel algorithm. Both your sequential and parallel solutions will take the following command-line arguments: \par
\bullet  number of workers, 1 to 32. This argument will be ignored by the sequential solution. \bullet  number of bodies.\par
 \bullet  size of each body. \par
\bullet  number of time steps. \par
You may add additional command-line arguments as needed to help in your experimentation. Provide default values for these additional arguments when they are not present. Examples can include a random seed, a random selection of sizes if all the balls are not the same size, etc. The output of each program should be the execution time for the computational part. Read the clock after you have initialized all the bodies. Read the clock just before you create the processes (in the parallel program). Read the clock again as soon as you have completed the time steps and the worker processes have terminated (in the parallel programs). Write to standard out: computation time: xxxx seconds xxxx milliseconds and the number of collisions detected. Write the \u-1279?nal positions and velocities to a \u-1279?le. \par
\par
Timing Experiments: \par
For your sequential program, use a test case that will take at most a few minutes to complete on one of the multi-core machines in the CS department labs. Now, use the same test case for your parallel solution. Run tests to determine the computation time needed for one, two, three, four, etc. workers. You should run each individual test more than once to get an average (and to allow you to \u-1279?lter out occasional timing \u-1278?uctuations that occur due to system routines). Your report should include a table and/or a chart showing the results of the timing runs that you perform. Include enough information so that we can replicate your results. We should be able to compile and run your code on the machine you used and see results close to what you report. \par
\par
\par
Report:\par
 Once you have done the timing and other experiments, write a report to explain what you have done and what you have learned. Your report should be a few pages of text plus tables and/or \u-1279?gures. It should have four or \u-1279?ve sections, as follows:\par
 \bullet  Introduction. Brie\u-1278?y describe the problem and what your report will show.\par
 \bullet  Programs. Describe your programs. Here is where you can describe your program-level optimizations for the sequential and parallel versions. Describe any enhancements in your solution. \par
\bullet  Veri\u-1279?cation. Describe how you veri\u-1279?ed that your program was working correctly. How do you know that the gravity force and velocities are correct? How do you know that collisions are resolved correctly? \par
\bullet  Timing Experiments. Present the results from the timing experiments. Use tables to present the raw data and graphs to show speedups and comparisons. Also explain your results. Do not just present the output data. What do the results show? Why? \par
\bullet  Other Experiments. Describe the questions that you set out to answer, the experiments you conducted, the results you got, and your analysis of the results. Present the results in whatever form seems most compelling to you. Your analysis should explain why you think you got the results you did.\par
 \bullet  Conclusion. Brie\u-1278?y summarize what your report has shown. Also, describe what you have learned from this projec\par
\par
Group vs. Solo:\par
 If you are working with a partner, we expect a better project than if you work solo. \ldblquote Better\rdblquote  can be in terms of quality or quantity. Examples include performing more experiments, extending the solution to three dimensions with a graphical interface, writing both a C and a Java solution and comparing their performance, measuring the performance of speci\u-1279?c sections of your program (such as the barriers mentioned above, how much time is spent in detecting and resolving collisions vs. force and velocity calculations) etc. You are welcome to discuss options with us in advance.\f0\par
}
 