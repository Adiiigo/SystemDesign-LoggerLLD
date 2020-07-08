# LoggerLLD
Design a low level desing of logger - asked in google

### Problem Statement

- Logger will have three functions through which we can implemet the functionality
- Logger has start function which is used to insert the processes
- Logger has end function which will used to end the processes
- Logger has poll function which is used to take the process which has ended with the conidition that it should be sorted on the basis of the starttime

##### Classes:
- Added the interface to abstract the implementation
- Added implementation class
- Created class to store Process
- Logger as the main class which will call all the functions to see the output

##### Data Structure used 
- We have used map to store the processes keyyed with processId but this is only used for storing and will not help in getting out the processed on polling
- We have used TreeMap to store the process keyyed on startTime and can be used to take out the processes based on startTime.

##### Poll function:
- if the queue is empty this means no process has started yet
- else, we will get first process and only if that process has ended(ascending order of that start time)
we will print it and remove it from the map and heap
-  else we will say the processes has no ended

##### Key insight:
- Even if some process has started after first process and ended before first process. we will still not print it as the starttime of the second process is greater than first process and has started late and it will wait for the first process to end and then only it will be eligible to get of the queue and polled, till then it will be held inside the queue.

##### Output after running the code:

```
Queue is empty and no process has started yet
No completed tasks in queue:1
No completed tasks in queue:2
No completed tasks in queue:2
No completed tasks in queue:2
No completed tasks in queue:2
3 started at 1594191238206 and ended at 1594191238207
1 started at 1594191238207 and ended at 1594191238207
Queue is empty and no process has started yet
```

Here we cannot see the output with second process beacuse it started and ended so quickly that it didnt even get time to get processed and went through the queue - My understanding can change with time and more knowledge
