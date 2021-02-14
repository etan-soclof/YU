package edu.yu.cs.intro;
public class Stopwatch {


    //instance variables
    private long startTime; //last time started
    private long accumulatedTime; //time accumulated
    private boolean stopped; //true if not running

    /**creates a stopwatch object with 0 time elapsed */
    public Stopwatch() {
        this.accumulatedTime = 0;
        this.stopped = true;
    }

    public Stopwatch(long num) throws IllegalArgumentException {

    	if (num < 0){
    		throw new IllegalArgumentException ("I am unhappy because you entered a number that is < 0");
    	}
    	else {
    		this.accumulatedTime = num;
   		}
    }

    /**starts the stopwatch*/
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.stopped = false;
    }
    /**stops the watch*/
    public void stop() {
        this.accumulatedTime += timeSinceStarted();
        this.stopped = true;
    }
    /**resets the accumulated time to 0*/
    public void reset() {
        this.accumulatedTime = 0;
        this.stopped = true;
    }
    /**
     * can be called whether watch is running or is stopped
     * @return the elapsed time
     */
    public long elapsed() {
        return this.accumulatedTime + timeSinceStarted();
    }

    public double elapsedSeconds() {
        return (double)elapsed()/1000;
    }

    //this is only used internally, not part of the public API, hence private
    private long timeSinceStarted() {
        return (this.stopped ? 0 : System.currentTimeMillis() - this.startTime);
    }
}