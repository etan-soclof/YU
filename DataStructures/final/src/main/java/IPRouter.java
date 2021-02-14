import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * IPRouter simulates the decision process for an IP router dispatching packets according a
 * prefix trie of routing rules.
 * 
 * @author Van Kelly
 * @version 1.0
 */
public class IPRouter
{

    final int nPorts; 
    final int cacheSize;
    final BitVectorTrie<Integer> trie = new BitVectorTrie<Integer>();
    private RouteCache cache;

    /** Router constructor
     * @param nPorts    the number of output ports, numbered 0 ... nPorts-1.  Pseudo-port -1 is 
     *                  always used for errors.
     * @param cacheSize the number of IP Addresses to be kept in a cache of the most recently routed 
     *                  UNIQUE IP Addresses
     */
    public IPRouter (int nPorts, int cacheSize) {
        this.nPorts = nPorts;
        this.cacheSize = cacheSize;
        this.cache = new RouteCache(this.cacheSize);
    }

    /**
     * Add a routing rule to the router. Each rule associates an IP Address prefix with an output port.
     * In case rules overlap, longest prefix wins.  If two rules specify exactly the same prefix, then
     * the second rule triggers an IllegalArgumentException.  The port must be in the permitted range
     * for this router, or an IllegalArgumentException will be triggered as well.
     * 
     * @param  prefix    an IP Address prefix in CIDR (dotted decimal) notation
     * @param  port
     * @return        true if rule is accepted. 
     */
    public void addRule(String prefix, int port) {
        if (port < 0 || port >= this.nPorts){
            throw new IllegalArgumentException("Invalid Port");
        }
        this.trie.put(new IPAddress(prefix), port);
    }

    public void deleteRule(String prefix) {
    	this.trie.delete(new IPAddress(prefix));
    }

    /**
     * Simulate routing a packet to its output port based on a binary IP Address.
     * If no rules apply to an address, route it to port -1 and log an error to System.err
     * 
     * @param  address    an IP Address object
     * @return  number of output port 
     */
    public int getRoute(IPAddress address) {
    	// finish this (~6 lines)
        Integer port = trie.get(address);
        if (port == null){
            this.cache.updateCache(address, -1);
            System.err.println("Address " + address.toCIDR() + " does not match a prefix");
            return -1;
        }
        this.cache.updateCache(address, port);
    	return port; //just so it compiles
    }

    /**
     * Tell whether an IP Address is currently in the cache of most recently routed addresses
      * 
     * @param  address    an IP Address in dotted decimal notation
     * @return  number of output port 
     */
    boolean isCached(IPAddress address) {
        return false;  // just so it compiles
    }
    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses.  Return a zero length array if the cache is empty
     * 
     */
    String[] dumpCache()
    {
        String[] array = this.cache.dumpQueue();
        if (array[0] == null){
            return new String[0];
        }
        return array;   // just so it compiles
    }
    
    /**
     * For testing and debugging, load a routing table from a text file
     * 
     */
    public void loadRoutes(String filename) throws FileNotFoundException
    {
        Scanner sc = new Scanner(new File(filename));
        BitVector bv = null;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("+")) {
                String[] pieces = line.substring(1).split(",");
                int port = Integer.parseInt(pieces[1]);
                bv = new IPAddress(pieces[0].trim());
                //System.out.println(pieces[0].trim() + " = " + bv.toString() + ", port: " + port);
                this.addRule(pieces[0].trim(), port);
            }
        }
    }
}
