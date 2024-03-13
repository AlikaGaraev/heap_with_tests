
import java.util.LinkedList;

public class BinomialHeapUtils {

    /**
     * 
     * @param numInserted: the number of insertions before calling this method
     * @param numDeleted:  the number of deletions before calling this method
     * @pre: numInserted >= numDeleted
     */

    public boolean checkNumTrees(int numInserted, int numDeleted, BinomialHeap hp) {
        int size = numInserted - numDeleted;
        int res = 0;
        while (size > 0) {
            int b = size % 2;
            res = b == 1 ? res + 1 : res;
            size = size / 2;
        }

        return res == hp.numTrees();
    }

    /**
     * 
     * @param numInserted: the number of insertions before calling this method
     * @param numDeleted:  the number of deletions before calling this method
     * @pre: numInserted >= numDeleted
     */
    public boolean checkSize(int numInserted, int numDeleted, BinomialHeap hp) {
        return numInserted - numDeleted == hp.size();
    }

    /**
     * 
     * @param keyOfMinNode: the key of the smallest inserted node up till now
     * 
     */
    public boolean checkMin(int keyOfMinNode, BinomialHeap hp) {
        return keyOfMinNode == hp.min.item.key;
    }

    /**
     * 
     * Check if the ranks of roots after melding two heaps are as expected
     * 
     */
    public boolean checkValidRootStructureAfterMeld(BinomialHeap hp1, BinomialHeap hp2) {
        LinkedList<Integer> l1 = new LinkedList<>();
        LinkedList<Integer> l2 = new LinkedList<>();
        BinomialHeap.HeapNode start1 = hp1.last.next;
        BinomialHeap.HeapNode start2 = hp2.last.next;
        hp1.last.next = null;
        hp2.last.next = null;
        int curIdx1 = 0;
        int curIdx2 = 0;

        BinomialHeap.HeapNode cur1 = start1;
        BinomialHeap.HeapNode cur2 = start2;
        while (cur1 != null) {
            if (cur1.rank != curIdx1) {
                l1.add(0);
                curIdx1 += 1;
                continue;
            }
            l1.add(1);
            curIdx1 += 1;
            cur1 = cur1.next;
        }
        while (cur2 != null) {
            if (cur2.rank != curIdx2) {
                l2.add(0);
                curIdx2 += 1;
                continue;
            }
            l2.add(1);
            curIdx2 += 1;
            cur2 = cur2.next;
        }
        while (l1.size() < l2.size()) {
            l1.add(0);
        }
        while (l2.size() < l1.size()) {
            l2.add(0);
        }
        l1.add(0);
        l2.add(0);

        int bit = 0;
        int carry = 0;
        int length = l1.size() > l2.size() ? l1.size() : l2.size();
        LinkedList<Integer> resultRanks = new LinkedList<>();
        while (bit < length) {
            int bit1 = l1.getFirst();
            int bit2 = l2.getFirst();
            if (bit1 + bit2 == 2) {
                if (carry == 1) {
                    resultRanks.add(bit);
                }
                carry = 1;
            } else if (bit1 + bit2 == 0) {
                if (carry == 1) {
                    resultRanks.add(bit);
                    carry = 0;
                }
            } else if (bit1 + bit2 == 1) {
                if (carry == 0) {
                    resultRanks.add(bit);
                } else {
                    carry = 1;
                }
            }
            bit += 1;
            l1.removeFirst();
            l2.removeFirst();
        }

        hp1.last.next = start1;
        hp2.last.next = start2;
        hp1.meld(hp2);
        start1 = hp1.last.next;
        hp1.last.next = null;
        cur1 = start1;
        while (resultRanks.size() > 0) {
            if (cur1 == null)
                return false;
            int rank = resultRanks.getFirst();
            if (rank != cur1.rank)
                return false;
            resultRanks.removeFirst();
            cur1 = cur1.next;
        }
        if (cur1 != null)
            return false;

        return true;
    }

    public static BinomialHeap createBinomialHeapFromInsertions(int[] insertions) {
        BinomialHeap bh = new BinomialHeap();
        for (int key : insertions) {
            bh.insert(key, null);
        }
        return bh;
    }

}
