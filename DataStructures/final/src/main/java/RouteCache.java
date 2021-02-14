import java.util.HashMap;
import java.util.Map;

/**
 * This is a bounded cache that maintains only the most recently accessed IP Addresses
 * and their routes.  Only the least recently accessed route will be purged from the
 * cache when the cache exceeds capacity.  There are 2 closely coupled data structures:
 *   -  a Map keyed to IP Address, used for quick lookup
 *   -  a Queue of the N most recently accessed IP Addresses
 * All operations must be O(1).  A big hint how to make that happen is contained
 * in the type signature of the Map on line 38.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RouteCache
{
    // instance variables - add others if you need them
    // do not change the names of any fields as the test code depends on them
    
    // Cache total capacity and current fill count.
    private final int capacity;
    private int nodeCount = 0;
    
    // private class for nodes in a doubly-linked queue
    // used to keep most-recently-used data
    private class Node {
        private Node prev, next;
        private final IPAddress elem; 
        private final int route;

        Node(IPAddress elem, int route) {
            prev = next = null;
            this.elem = elem;
            this.route = route;
        }  
    }
    private Node head = null;
    private Node tail = null;
    private Map<IPAddress, Node> nodeMap = new HashMap<>(); // the cache itself

    /**
     * Constructor for objects of class RouteCache
     */
    public RouteCache(int cacheCapacity) {
    	this.capacity = cacheCapacity;
    }

    /**
     * Lookup the output port for an IP Address in the cache
     * 
     * @param  addr   a possibly cached IP Address
     * @return     the cached route for this address, or null if not found 
     */
    public Integer lookupRoute(IPAddress addr) {
    	if (!(this.nodeMap.containsKey(addr))){
    	    return null;
        }
    	return this.nodeMap.get(addr).route;
     }
     
    /**
     * Update the cache each time an element's route is looked up.
     * Make sure the element and its route is in the Map.
     * Enqueue the element at the tail of the queue if it is not already in the queue.  
     * Otherwise, move it from its current position to the tail of the queue.  If the queue
     * was already at capacity, remove and return the element at the head of the queue.
     * 
     * @param  elem  an element to be added to the queue, which may already be in the queue. 
     *               If it is, don't add it redundantly, but move it to the back of the queue
     * @return       the expired least recently used element, if any, or null
     */
    public IPAddress updateCache(IPAddress elem, int route) {
        //If the Address is already in the cache just move it to the head
    	if (this.nodeMap.get(elem) != null){
    	    //If there is only one node in the cache, do nothing
    	    if (this.nodeCount == 1){
    	        return null;
            }
    	    //If there are two nodes in the cache, if @elem is already the tail, do nothing. If not, switch the head and tail
    	    if (this.nodeCount == 2){
    	        if (this.tail.elem.equals(elem)){
    	            return null;
                }
    	        Node holder = this.tail;
    	        this.tail = this.head;
    	        this.head = holder;
    	        this.tail.prev = this.head;
    	        this.head.next = this.tail;
    	        return null;
            }
    	    //Remove the target from the cache by connecting its previous and next
    	    Node target = this.nodeMap.get(elem);
    	    if(target.equals(this.tail)){
    	        return null;
            }
            Node holder = target.prev;
    	    holder.next = target.next;
    	    target.next.prev = holder;
    	    //Now move the target to the tail
            holder = this.tail;
    	    holder.next = target;
    	    this.tail = target;
    	    this.tail.prev = holder;
        }
    	//If @elem is not in the cache, set it equal to the tail
    	else{
            if (this.capacity == 1){
                this.tail = new Node (elem, route);
            }
    	    if (this.tail == null){
    	        this.tail = new Node (elem, route);
    	        this.tail.prev = this.head;
    	        this.nodeCount++;
    	        this.nodeMap.put(elem, this.tail);
    	        return null;
            }
    	    if (this.head == null){
                this.head = this.tail;
                this.tail = new Node (elem, route);
                this.tail.prev = this.head;
                this.head.next = this.tail;
                this.nodeMap.put(elem, this.tail);
                this.nodeCount++;
                return null;
            }
    	    Node node = new Node (elem, route);
            Node holder = this.tail;
            holder.next = node;
            this.tail = node;
            this.tail.prev = holder;
            this.nodeMap.put(elem, this.tail);
            this.nodeCount++;
            //Check that it has not exceeded capacity
            if (this.nodeCount > this.capacity){
                holder = this.head;
                this.head = this.head.next;
                this.nodeMap.remove(elem);
                this.nodeCount--;
                return holder.elem;
            }
        }
    	return null;
    }

    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses in CIDR format. Return a zero length array if the cache is empty
     * 
     */
    String[] dumpQueue() {
        Node node = this.tail;
        String [] array = new String[this.nodeCount];
    	for (int i = 0; i < this.nodeCount; i++){
    	    if (node != null){
                array[i] = node.elem.toCIDR();
                node = node.prev;
            }
        }
    	return array; //placeholder
    }
    
    
}
