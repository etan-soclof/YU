public class TrieImplTest {
    /*

    private Comparator<Integer> comparator = (Integer x, Integer y) -> {
        if (x > y){
            return 1;
        }
        if (x < y){
            return -1;
        }
        return 0;
    };

    @Test
    public void TrieTest() {
        TrieImpl<Integer> trie = new TrieImpl<>();
        trie.put("ab", 1);
        trie.put("ab", 2);
        trie.put("ab", 3);
        trie.put("ab", 4);
        trie.put("abc", 5);
        trie.put("a", 6);
        //trie.put("akjkls", 5);
        //trie.put("nothappening", 6);

        List<Integer> list1 = trie.getAllSorted("ab", comparator);
        ListIterator<Integer> itr1 = list1.listIterator();
        while(itr1.hasNext()){
            System.out.println(itr1.next());
        }
        List<Integer> list = trie.getAllWithPrefixSorted("ab", comparator);
        ListIterator<Integer> itr = list.listIterator();
        while(itr.hasNext()){
           System.out.println(itr.next());
        }
        int x = 0;
        trie.deleteAll("ab");
        //assertNull("should be null", trie.getAllSorted("ab", comparator));
        list1 = trie.getAllSorted("abc", comparator);
        itr1 = list1.listIterator();
        while(itr1.hasNext()){
            System.out.println(itr1.next());
        }
        list1 = trie.getAllSorted("a", comparator);
        itr1 = list1.listIterator();
        while(itr1.hasNext()){
            System.out.println(itr1.next());
        }
    }

    @Test
    public void testDelete(){
        TrieImpl<Integer> trie1 = new TrieImpl<>();
        trie1.put("ab", 1);
        trie1.put("ab", 2);
        trie1.put("ab", 3);
        trie1.put("ab", 4);
        trie1.put("abc", 5);
        trie1.put("a", 6);
        List<Integer> set = new ArrayList<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        assertEquals(trie1.getAllSorted("ab", this.comparator), set);
        trie1.delete("ab", 2);
        set.remove((Integer) 2);
        assertEquals(set, trie1.getAllSorted("ab", this.comparator));
        set.remove((Integer) 1);
        set.remove((Integer) 3);
        set.remove((Integer) 4);
        trie1.deleteAll("ab");
        assertEquals(set, trie1.getAllSorted("ab", this.comparator));
        set.add(5);
        assertEquals(set, trie1.getAllSorted("abc", this.comparator));
    }

    @Test
    public void testDeleteAllWithPrefix(){
        TrieImpl<Integer> trie1 = new TrieImpl<>();
        trie1.put("ac", 1);
        trie1.put("ab", 2);
        trie1.put("ab", 3);
        trie1.put("ab", 4);
        trie1.put("abc", 5);
        trie1.put("a", 6);
        List<Integer> set = new ArrayList<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        set.add(6);
        List<Integer> list = trie1.getAllWithPrefixSorted("a", comparator);
        assertEquals(set, list);
        trie1.deleteAllWithPrefix("ab");
        set.remove((Integer) 2);
        set.remove((Integer) 3);
        set.remove((Integer) 4);
        set.remove((Integer) 5);
        list = trie1.getAllWithPrefixSorted("a", comparator);
        assertEquals(set, list);

    }

     */
}
