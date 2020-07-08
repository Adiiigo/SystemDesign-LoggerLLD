import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

interface LogClient{
	/*
	 * When a process starts, it calls "start" with processID
	 */
	void start(String processID);
	
	/*
	 * When the same process ends, it calls "end" with processID
	 */
	void end(String processID) ;
	
	/*
	 * Polls the first log entry of a completed process sorted by the start time
	 * of the process in the below format
	 * 
	 * {ProcessId} started at {startTime} and ended at {endTime}
	 * <p>
	 * process id = 1 -> 12 , 15
	 * process id = 2 -> 8 , 12 
	 * process id = 3 -> 7 , 19
	 * <p>
	 * {3} started at {7} and ended at {19}
	 * {2} started at {8} and ended at {12}
	 * {1} started at {12} and ended at {15}
	 */
	void poll() ;
}

class LoggerImplementation implements LogClient{

	/*
	 * We will be using map to store processId as key and ProcessInfo as value
	 */
	private final Map<String , Process> Processes ;
	
	/*
	 * Using heap/priority queue/ordered map to store the start time as key 
	 * and corresponding processInfo as value
	 */
	private final TreeMap<Long, Process> queue ;
	
	public LoggerImplementation() {
		// TODO Auto-generated constructor stub
		this.Processes = new HashMap<>();
		this.queue = new TreeMap<>(Comparator.comparingLong(startTime -> startTime)) ;
	}
	
	@Override
	public void start(String processID) {
		// TODO Auto-generated method stub
		
		final long now = System.currentTimeMillis();
		final Process process = new Process(processID , now) ;
		Processes.put(processID, process) ;

		queue.put(now, process) ;
	}

	@Override
	public void end(String ProcessID) {
		// TODO Auto-generated method stub
		
		/*
		 * As the process has ended, we only have to updated the information of that processId
		 * updating the queue will not matter here as we only have to remove the processId on 
		 * the basis of the startTime and not on the end Time
		 * 
		 * If we were given that, we have to poll the process on the basis of the end time
		 * then we would have updated the queue
		 */
		
		final long now = System.currentTimeMillis() ;
		Processes.get(ProcessID).setEndTime(now) ;
	}

	@Override
	public void poll() {
		// TODO Auto-generated method stub
		/*
		 * This "if" statement says that there has no process which has started yet
		 */
		if(queue.isEmpty()) {
			System.out.println("Queue is empty and no process has started yet");
			return ;
		}
		
		
		final Process process = queue.firstEntry().getValue() ;
		if(process.getEndTime() != -1) {
			/*
			 * This means the process has ended
			 */
			System.out.println(process.getId() + " started at " + process.getStartTime() + " and ended at " + process.getEndTime()) ;
			Processes.remove(process.getId()) ;
			queue.pollFirstEntry() ;
		}
		else {
			System.out.println("No completed tasks in queue:" + queue.size());
		}
	}
	
}

class Process{
	
	private final String id ;
	private final long startTime ;
	private long endTime ;
	
	public Process(String id , final long startTime) {
		this.id = id ;
		this.startTime = startTime ;
		endTime = -1 ;
	}
	
	public String getId() {
		return id ;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getStartTime() {
		return startTime;
	}
	
}

public class Logger {
	/*
     * {3} started at {7} and ended at {19}
     * {2} started at {8} and ended at {12}
     * {1} started at {12} and ended at {15}
     */
	
	public static void main(String[] args) {
		final LogClient logClient = new LoggerImplementation() ;
		logClient.poll(); // no process is there inside the queue
		logClient.start("3");
		logClient.poll(); // process 3 started and added in map and queue
		logClient.start("2");
		logClient.poll() ; // process 2 started and added in map and queue
		logClient.end("2");
		logClient.poll(); // process 2 ended and still in map and queue
		logClient.start("1");
		logClient.poll(); // process 1 started and added in map and queue
		logClient.end("1");
		logClient.poll(); //process 1 ended and still in map and queue
		logClient.end("3");
		logClient.poll() ; //process 3 ended and removed from map and queue
		logClient.poll() ; //process 2 ended and removed from map and queue
		logClient.poll() ; //process 1 ended and removed from map and queue
		
	}
}
