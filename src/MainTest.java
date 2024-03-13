import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    @DisplayName("result of 8 elements")
    public void test1() {
        int[] insertions = { 1, 2, 3, 4, 5, 6, 1, 2 };
        BinomialHeap bh = BinomialHeapUtils.createBinomialHeapFromInsertions(insertions);
        System.out.println("num of trees is" + bh.treeNum);
        Assertions.assertEquals(1, bh.numTrees(), "Number of roots should be equal to 1");
    }

    @Test
    @DisplayName("result of 8 elements with deletions")
    public void test2() {
        int[] insertions = { 1, 2, 10, 4, 5, 6, 1, 2 };
        BinomialHeap bh = BinomialHeapUtils.createBinomialHeapFromInsertions(insertions);
        BinomialHeap.HeapItem it1 = bh.insert(1, null);
        BinomialHeap.HeapItem it2 = bh.insert(1, null);
        bh.delete(it2);
        bh.delete(it1);
        Assertions.assertEquals(1, bh.numTrees(), "Number of Trees  should be equal to 1");
    }

    @Test
    @DisplayName("result of 2^n - 1 elements")
    public void test3() {
        int[] n = { 7, 15, 31, 63, 127 };
        for (int m : n) {
            int[] array = new int[m];
            Arrays.setAll(array, i -> i + 1);
            BinomialHeap bh = BinomialHeapUtils.createBinomialHeapFromInsertions(array);
            Assertions.assertEquals((int) (Math.log(m) / Math.log(2)) + 1, bh.numTrees(),
                    "Number of Trees Should be equal to log(n) where n is a perfect power of 2.");
        }
    }

    @Test
    @DisplayName("result of 2^n elements")
    public void test4() {
        int[] n = { 8, 16, 32, 64, 128, 1024 };
        for (int m : n) {
            int[] array = new int[m];
            Arrays.setAll(array, i -> i + 1);
            BinomialHeap bh = BinomialHeapUtils.createBinomialHeapFromInsertions(array);
            Assertions.assertEquals(1, bh.numTrees(),
                    "Number of Trees Should be equal to 1");
        }
    }

    @Test
    @DisplayName("Should be 3")
    public void test5() {
        int[] array = new int[67];
        Arrays.setAll(array, i -> i + 1);
        BinomialHeap bh = BinomialHeapUtils.createBinomialHeapFromInsertions(array);
        Assertions.assertEquals(3, bh.numTrees(), "Number of Trees Should be equal to 3");
    }

    @Test
    @DisplayName("Should be 6") // 0010101010101
    public void test6() {
        int[] array = new int[1365];
        Arrays.setAll(array, i -> i + 1);
        BinomialHeap bh = BinomialHeapUtils.createBinomialHeapFromInsertions(array);
        Assertions.assertEquals(6, bh.numTrees(), "Number of Trees Should be equal to 6");
    }

    @Test
    @DisplayName("should be 0")
    public void test7() {
        BinomialHeap bh = new BinomialHeap();
        Assertions.assertEquals(0, bh.size(), "Size of empty tree should be 0");
    }

    @Test
    @DisplayName("insertions only size check")
    public void test8() {
        int n = 300;
        BinomialHeap bh = new BinomialHeap();
        for (int m = 0; m < n; m++) {
            bh.insert(m, null);
            Assertions.assertEquals(m + 1, bh.size(),
                    "Size should be " + m);
        }
    }

    @Test
    @DisplayName("size check with deletions")
    public void test9() {
        int n = 300;
        LinkedList<BinomialHeap.HeapItem> items = new LinkedList<>();
        BinomialHeap bh = new BinomialHeap();
        for (int m = 0; m < n; m++) {
            int op = 1;

            // There is at least one element in the heap
            if (!items.isEmpty()) {
                Random rd = new Random();
                op = rd.nextInt(2);
            }
            if (op == 1) {
                items.add(bh.insert(m, null));
            } else {
                bh.deleteMin();
                items.removeFirst();
            }
            Assertions.assertEquals(items.size(), bh.size(),
                    "Size should be " + items.size());
        }
    }

    @Test
    @DisplayName("Melding with empty heap")
    public void test10() {
        BinomialHeap bh1 = new BinomialHeap();
        BinomialHeap bh2 = new BinomialHeap();
        BinomialHeap.HeapNode min = bh1.insert(0, null).node;
        bh1.meld(bh2);

        Assertions.assertEquals(1, bh1.size(), "The size is not 1");
        Assertions.assertSame(min, bh1.min, "the min value was not updated after melding");
        Assertions.assertSame(min, bh1.last, "the value of last is not updated");
        Assertions.assertSame(min, bh1.last.next, "the last root is not connected to itsself");
    }

    @Test
    @DisplayName("Melding empty heap with empty heap")
    public void test11() {
        BinomialHeap bh1 = new BinomialHeap();
        BinomialHeap bh2 = new BinomialHeap();
        bh1.meld(bh2);

        Assertions.assertEquals(0, bh1.size(), "The size is not 0");
        // cant test more stuff because i cant make assumptions of programmer's
        // implementation of an empty heap.
    }

    @Test
    @DisplayName("Melding empty with non-empty heap")
    public void test12() {
        BinomialHeap bh1 = new BinomialHeap();
        BinomialHeap bh2 = new BinomialHeap();
        BinomialHeap.HeapNode min = bh2.insert(0, null).node;
        bh1.meld(bh2);
        System.out.println("Size of bh1 is " +bh1.size);
        Assertions.assertEquals(1, bh1.size(), "The size is not 1");
        System.out.println("Value of min is "+ bh1.min.item.key);
        Assertions.assertSame(min, bh1.min, "the min value was not updated after melding");
        System.out.println("Value of last is " + bh1.last.item.key);
        Assertions.assertSame(min, bh1.last, "the value of last is not updated");
        System.out.println("Value of head is "+ bh1.last.next.item.key);
        Assertions.assertSame(min, bh1.last.next, "the last root is not connected to itsself");
    }

    @Test
    @DisplayName("Fill binary represenation with 1's")
    public void test13() {
        String roots1 = "101010101010";
        int n = Integer.parseInt(roots1, 2);

        String roots2 = "010101010101";
        int m = Integer.parseInt(roots2, 2);

        BinomialHeap bh1 = new BinomialHeap();
        BinomialHeap bh2 = new BinomialHeap();
        for (int i = 0; i < n; i++) {
            bh1.insert(i, null);
        }
        for (int i = 0; i < m; i++) {
            bh2.insert(i, null);
        }
        bh1.meld(bh2);

        Assertions.assertNotNull(bh1.last);
        Assertions.assertEquals(n + m, bh1.size());
        Assertions.assertNotSame(bh1.last, bh1.last.next, "Last is connected to itsself");
        BinomialHeap.HeapNode start = bh1.last.next;
        bh1.last.next = null;
        BinomialHeap.HeapNode cur = start;
        int curRank = 0;
        while (cur != null) {
            Assertions.assertEquals(curRank, cur.rank, "rank is supposed to be" + curRank);
            curRank += 1;
            cur = cur.next;
        }
        bh1.last.next = start;
    }

    @Test
    @DisplayName("Dominos to pefect power of 2")
    public void test14() {
        String roots1 = "1111111110";
        int n = Integer.parseInt(roots1, 2);

        String roots2 = "10";
        int m = Integer.parseInt(roots2, 2);

        BinomialHeap bh1 = new BinomialHeap();
        BinomialHeap bh2 = new BinomialHeap();
        for (int i = 1; i <= n; i++) {
            bh1.insert(i, null);
        }
        for (int i = 1; i <= m; i++) {
            bh2.insert(i, null);
        }
        bh1.meld(bh2);

        System.out.println(n);

        Assertions.assertNotNull(bh1.last);
        Assertions.assertEquals(n + m, bh1.size());
        Assertions.assertSame(bh1.last, bh1.last.next, "Last is not connected to itsself");
        Assertions.assertEquals(10, bh1.last.rank);
    }

    @DisplayName("Checking tree structure after a sequence of insertions and deletions")
    @Test
    public void test15() {
        BinomialHeap bh = new BinomialHeap();
        bh.insert(1, "0");
        Assertions.assertEquals(1, bh.size());
        Assertions.assertNotNull(bh.min);
        Assertions.assertNotNull(bh.last);
        Assertions.assertEquals("0", bh.min.item.info, "Min is not updated");
        Assertions.assertEquals("0", bh.last.item.info, "Last is not updated");
        bh.deleteMin();
		Assertions.assertEquals(0, bh.size(), "The tree size should be 0");

        bh.insert(1, "0");
        BinomialHeap.HeapItem x = bh.insert(2, "1");
        bh.insert(3, "2");
        Assertions.assertNotNull(bh.min, "Min shouldn't be null");
        Assertions.assertNotNull(bh.last, "Last shouldnt be null");
        Assertions.assertEquals(2, bh.numTrees());
        Assertions.assertEquals(0, bh.last.next.rank);
        Assertions.assertSame(bh.last, bh.last.next.next);
        Assertions.assertEquals(1, bh.last.rank);
        Assertions.assertEquals(0, bh.last.child.rank);
        Assertions.assertTrue(bh.last.child.item.key > bh.last.child.parent.item.key);
        Assertions.assertSame(bh.last.next.next.child, bh.last.next.next.child.next);

        bh.delete(x);
        Assertions.assertNotNull(bh.min);
        Assertions.assertNotNull(bh.last);
        Assertions.assertEquals(1, bh.numTrees());
        Assertions.assertEquals(2, bh.size());
        Assertions.assertSame(bh.last, bh.last.next);
        Assertions.assertEquals(1, bh.last.rank);
        Assertions.assertEquals(1, bh.last.item.key);
        Assertions.assertEquals(3, bh.last.child.item.key);
        Assertions.assertSame(bh.last, bh.last.child.parent);
        Assertions.assertSame(bh.last.child, bh.last.child.next);
        Assertions.assertEquals(1, bh.min.item.key);

        bh.insert(2, "1");

        Assertions.assertNotNull(bh.min);
        Assertions.assertNotNull(bh.last);
        Assertions.assertEquals(1, bh.min.item.key);
        Assertions.assertEquals(2, bh.numTrees());
        Assertions.assertEquals(3, bh.size());
        Assertions.assertEquals(2, bh.last.next.item.key);
        Assertions.assertEquals(0, bh.last.next.rank);
        Assertions.assertEquals(1, bh.last.rank);
        Assertions.assertEquals(1, bh.last.item.key);
        Assertions.assertEquals(3, bh.last.child.item.key);
        Assertions.assertSame(bh.last.child, bh.last.child.next);
    }

    @DisplayName("Checking tree structure after a sequence of insertions and deletions of minimum")
    @Test
    public void test16() {
        BinomialHeap bh = new BinomialHeap();
        bh.insert(2, null);
        bh.insert(3, null);
        bh.insert(2, null);
        bh.insert(1, null);
        bh.insert(5, null);
        bh.insert(0, null);
        bh.insert(7, null);

        Assertions.assertNotNull(bh.min);
        Assertions.assertNotNull(bh.last);
        Assertions.assertEquals(7, bh.size());
        Assertions.assertEquals(3, bh.numTrees());

        Assertions.assertEquals(0, bh.last.next.rank);
        Assertions.assertEquals(7, bh.last.next.item.key);
        Assertions.assertEquals(0, bh.last.next.next.item.key);
        Assertions.assertEquals(1, bh.last.next.next.rank);
        Assertions.assertEquals(5, bh.last.next.next.child.item.key);
        Assertions.assertEquals(5, bh.last.next.next.child.next.item.key);
        Assertions.assertSame(bh.last.next.next, bh.last.next.next.child.parent);
        Assertions.assertSame(bh.last, bh.last.next.next.next);
        Assertions.assertEquals(2, bh.last.rank);
        Assertions.assertEquals(1, bh.last.item.key);
        Assertions.assertEquals(1, bh.last.child.rank);
        Assertions.assertEquals(2, bh.last.child.item.key);
        Assertions.assertEquals(3, bh.last.child.child.item.key);
        Assertions.assertEquals(2, bh.last.child.next.item.key);
        Assertions.assertNotSame(bh.last.child.next, bh.last.child);
        Assertions.assertEquals(0, bh.last.next.next.child.next.rank);
        Assertions.assertSame(bh.last.child.next.next, bh.last.child);

        Assertions.assertEquals(0, bh.min.item.key);
        bh.deleteMin();
        Assertions.assertEquals(1, bh.min.item.key);
        Assertions.assertEquals(5, bh.last.next.item.key);
        Assertions.assertEquals(1, bh.last.next.rank);
        Assertions.assertEquals(7, bh.last.next.child.item.key);
        Assertions.assertEquals(5, bh.last.next.child.parent.item.key);
        Assertions.assertEquals(0, bh.last.next.child.rank);
        Assertions.assertSame(bh.last, bh.last.next.next);
        Assertions.assertEquals(1, bh.last.item.key);
        Assertions.assertEquals(2, bh.last.rank);

        bh.insert(10, null);
        Assertions.assertEquals(3, bh.numTrees());
        Assertions.assertEquals(10, bh.last.next.item.key);
        Assertions.assertEquals(0, bh.last.next.rank);
        Assertions.assertEquals(5, bh.last.next.next.item.key);
        Assertions.assertSame(bh.last, bh.last.next.next.next);

        bh.deleteMin();
        Assertions.assertEquals(2, bh.min.item.key);

        bh.deleteMin();
        Assertions.assertEquals(2, bh.min.item.key);

        bh.deleteMin();
        Assertions.assertEquals(3, bh.min.item.key);

        Assertions.assertEquals(1, bh.numTrees());
        Assertions.assertEquals(2, bh.last.rank);
    }

    @DisplayName("Focuses on verifying tree structure after trick deletes")
    @Test
    public void test17() {
        int[] insertions = { 5, 6, 7, 8, 4, 3, 2, 11 };
        HashMap<Integer, BinomialHeap.HeapItem> keyToNode = new HashMap<>();
        BinomialHeap bh = new BinomialHeap();
        for (int i : insertions) {
            keyToNode.put(i, bh.insert(i, null));
        }

        bh.delete(keyToNode.get(4));

        Assertions.assertEquals(3, bh.numTrees());
        Assertions.assertNotNull(bh.last);
        Assertions.assertNotNull(bh.min);
        Assertions.assertEquals(0, bh.last.next.rank);
        Assertions.assertEquals(1, bh.last.next.next.rank);
        Assertions.assertEquals(0, bh.last.next.next.child.rank);
        Assertions.assertTrue(bh.last.next.next.child.item.key > bh.last.next.next.item.key);
        Assertions.assertSame(bh.last, bh.last.next.next.next);
        Assertions.assertEquals(2, bh.last.rank);
        Assertions.assertEquals(1, bh.last.child.rank);
        Assertions.assertTrue(bh.last.child.item.key > bh.last.item.key);
        Assertions.assertEquals(0, bh.last.child.child.rank);
        Assertions.assertTrue(bh.last.child.child.item.key > bh.last.child.child.parent.item.key);
        Assertions.assertEquals(0, bh.last.child.next.rank);
        Assertions.assertTrue(bh.last.child.next.item.key > bh.last.child.next.parent.item.key);

        bh.delete(keyToNode.get(8));
        Assertions.assertEquals(2, bh.numTrees());
        Assertions.assertNotNull(bh.last);
        Assertions.assertNotNull(bh.min);
        Assertions.assertEquals(1, bh.last.next.rank);
        Assertions.assertTrue(bh.last.next.child.item.key > bh.last.next.item.key);
        Assertions.assertSame(bh.last, bh.last.next.next);
        Assertions.assertEquals(2, bh.last.rank);
        Assertions.assertTrue(bh.last.child.item.key > bh.last.item.key);
        Assertions.assertEquals(1, bh.last.child.rank);
        Assertions.assertTrue(bh.last.child.child.item.key > bh.last.child.item.key);
        Assertions.assertEquals(0, bh.last.child.child.rank);
        Assertions.assertTrue(bh.last.child.next.item.key > bh.last.item.key);
        Assertions.assertEquals(0, bh.last.child.next.rank);
        Assertions.assertEquals(2, bh.min.item.key);

        Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");

        Assertions.assertEquals(3, bh.min.item.key);

        Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");
        Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");
        Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");
        Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");
        Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");

        Assertions.assertEquals(0, bh.size());
        Assertions.assertEquals(0, bh.numTrees());
    }

    @DisplayName("Checking preservation of tree values after a sequence of insert and delete")
    @Test
    public void test18() {
        int[] insertions = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 4, 3, 2, 1, 0 };
        BinomialHeap bh = new BinomialHeap();

        for (int i : insertions) {
            bh.insert(i, null);
            Assertions.assertEquals(1, bh.size());
            Assertions.assertEquals(1, bh.numTrees());
            Assertions.assertEquals(i, bh.min.item.key);
            Assertions.assertEquals(i, bh.last.item.key);
            Assertions.assertEquals(0, bh.last.rank);
            Assertions.assertSame(bh.last, bh.last.next);
            Assertions.assertDoesNotThrow(bh::deleteMin, "delete min resulted into an exception");
        }
    }

    @DisplayName("Checks structure, ranks, values of tree after melding with another")
    @Test
    public void test19() {
        BinomialHeap bh1 = new BinomialHeap();
        for (int i = 1; i <= 15; i++) {
            bh1.insert(i, null);
        }

        BinomialHeap bh2 = new BinomialHeap();
        for (int i = 16; i <= 30; i++) {
            bh2.insert(i, null);
        }

        bh1.meld(bh2);
        System.out.println(bh1);
        Assertions.assertEquals(4, bh1.numTrees());
        Assertions.assertEquals(30, bh1.size());

        Assertions.assertEquals(1, bh1.last.next.rank);
        Assertions.assertEquals(0, bh1.last.next.child.rank);
        Assertions.assertTrue(bh1.last.next.child.item.key > bh1.last.next.item.key);

        Assertions.assertEquals(2, bh1.last.next.next.rank);

        Assertions.assertEquals(1, bh1.last.next.next.child.rank);
        Assertions.assertTrue(bh1.last.next.next.child.item.key > bh1.last.next.next.item.key);
        Assertions.assertEquals(0, bh1.last.next.next.child.child.rank);
        Assertions.assertTrue(bh1.last.next.next.child.child.item.key > bh1.last.next.next.child.item.key);
        Assertions.assertEquals(0, bh1.last.next.next.child.next.rank);
        Assertions.assertTrue(bh1.last.next.next.child.next.item.key > bh1.last.next.next.item.key);

        Assertions.assertEquals(3, bh1.last.next.next.next.rank);
        Assertions.assertEquals(2, bh1.last.next.next.next.child.rank);
        Assertions.assertTrue(bh1.last.next.next.next.child.item.key > bh1.last.next.next.next.item.key);

        Assertions.assertEquals(1, bh1.last.next.next.next.child.child.rank);
        Assertions.assertTrue(bh1.last.next.next.next.child.child.item.key > bh1.last.next.next.next.child.item.key);
        Assertions.assertEquals(0, bh1.last.next.next.next.child.child.child.rank);
        Assertions.assertTrue(
                bh1.last.next.next.next.child.child.child.item.key > bh1.last.next.next.next.child.child.item.key);
        Assertions.assertEquals(0, bh1.last.next.next.next.child.child.next.rank);
        Assertions.assertTrue(
                bh1.last.next.next.next.child.child.next.item.key > bh1.last.next.next.next.child.child.next.parent.item.key);
        Assertions.assertEquals(0, bh1.last.next.next.next.child.next.rank);
        Assertions.assertTrue(
                bh1.last.next.next.next.child.next.item.key > bh1.last.next.next.next.child.next.parent.item.key);
        Assertions.assertEquals(1, bh1.last.next.next.next.child.next.next.rank);
        Assertions.assertTrue(
                bh1.last.next.next.next.child.next.next.item.key > bh1.last.next.next.next.child.next.next.parent.item.key);
        Assertions.assertEquals(0, bh1.last.next.next.next.child.next.next.child.rank);
        Assertions.assertTrue(
                bh1.last.next.next.next.child.next.next.child.item.key > bh1.last.next.next.next.child.next.next.child.parent.item.key);

        Assertions.assertSame(bh1.last, bh1.last.next.next.next.next);
        Assertions.assertEquals(4, bh1.last.rank);
        Assertions.assertEquals(3, bh1.last.child.rank);
        Assertions.assertTrue(bh1.last.child.item.key > bh1.last.item.key);
        Assertions.assertEquals(2, bh1.last.child.child.rank);
        Assertions.assertTrue(bh1.last.child.child.item.key > bh1.last.child.item.key);
        Assertions.assertEquals(1, bh1.last.child.child.child.rank);
        Assertions.assertTrue(bh1.last.child.child.child.item.key > bh1.last.child.child.item.key);
        Assertions.assertEquals(0, bh1.last.child.child.child.child.rank);
        Assertions.assertTrue(bh1.last.child.child.child.child.item.key > bh1.last.child.child.child.item.key);
        Assertions.assertEquals(0, bh1.last.child.child.child.next.rank);
        Assertions
                .assertTrue(bh1.last.child.child.child.next.item.key > bh1.last.child.child.child.next.parent.item.key);
        Assertions.assertEquals(0, bh1.last.child.child.next.rank);
        Assertions
                .assertTrue(bh1.last.child.child.next.item.key > bh1.last.child.child.next.parent.item.key);
        Assertions.assertEquals(1, bh1.last.child.child.next.next.rank);
        Assertions.assertTrue(bh1.last.child.child.next.next.item.key > bh1.last.child.child.next.next.parent.item.key);
        Assertions.assertEquals(0, bh1.last.child.child.next.next.child.rank);
        Assertions.assertTrue(
                bh1.last.child.child.next.next.child.item.key > bh1.last.child.child.next.next.child.parent.item.key);
        Assertions.assertEquals(0, bh1.last.child.next.rank);
        Assertions.assertTrue(bh1.last.child.next.item.key > bh1.last.child.next.parent.item.key);
        Assertions.assertEquals(1, bh1.last.child.next.next.rank);
        Assertions.assertTrue(bh1.last.child.next.next.item.key > bh1.last.child.next.next.parent.item.key);
        Assertions.assertEquals(0, bh1.last.child.next.next.child.rank);
        Assertions.assertTrue(bh1.last.child.next.next.child.item.key > bh1.last.child.next.next.child.parent.item.key);
        Assertions.assertEquals(2, bh1.last.child.next.next.next.rank);
        Assertions.assertTrue(bh1.last.child.next.next.next.item.key > bh1.last.child.next.next.next.parent.item.key);
        Assertions.assertEquals(1, bh1.last.child.next.next.next.child.rank);
        Assertions.assertTrue(
                bh1.last.child.next.next.next.child.item.key > bh1.last.child.next.next.next.child.parent.item.key);
        Assertions.assertEquals(0, bh1.last.child.next.next.next.child.child.rank);
        Assertions.assertTrue(
                bh1.last.child.next.next.next.child.child.item.key > bh1.last.child.next.next.next.child.child.parent.item.key);
        Assertions.assertEquals(0, bh1.last.child.next.next.next.child.next.rank);
        Assertions.assertTrue(
                bh1.last.child.next.next.next.child.next.item.key > bh1.last.child.next.next.next.child.next.parent.item.key);

        // OLD TESTS , DO NOT UNCOMMENT
        // Assertions.assertEquals(4, bh1.numTrees());
        // Assertions.assertEquals(30, bh1.size());

        // Assertions.assertEquals(1, bh1.last.next.rank);
        // Assertions.assertEquals(15, bh1.last.next.item.key);
        // Assertions.assertEquals(30, bh1.last.next.child.item.key);

        // Assertions.assertEquals(2, bh1.last.next.next.rank);
        // Assertions.assertEquals(13, bh1.last.next.next.item.key);
        // Assertions.assertEquals(28, bh1.last.next.next.child.item.key);
        // Assertions.assertEquals(29, bh1.last.next.next.child.child.item.key);
        // Assertions.assertEquals(14, bh1.last.next.next.child.next.item.key);

        // Assertions.assertEquals(3, bh1.last.next.next.next.rank);
        // Assertions.assertEquals(9, bh1.last.next.next.next.item.key);
        // Assertions.assertEquals(24, bh1.last.next.next.next.child.item.key);
        // Assertions.assertEquals(26, bh1.last.next.next.next.child.child.item.key);
        // Assertions.assertEquals(27,
        // bh1.last.next.next.next.child.child.child.item.key);
        // Assertions.assertEquals(25,
        // bh1.last.next.next.next.child.child.next.item.key);
        // Assertions.assertEquals(10, bh1.last.next.next.next.child.next.item.key);
        // Assertions.assertEquals(11,
        // bh1.last.next.next.next.child.next.next.item.key);
        // Assertions.assertEquals(12,
        // bh1.last.next.next.next.child.next.next.child.item.key);

        // Assertions.assertSame(bh1.last, bh1.last.next.next.next.next);
        // Assertions.assertEquals(1, bh1.last.item.key);
        // Assertions.assertEquals(16, bh1.last.child.item.key);
        // Assertions.assertEquals(20, bh1.last.child.child.item.key);
        // Assertions.assertEquals(22, bh1.last.child.child.child.item.key);
        // Assertions.assertEquals(23, bh1.last.child.child.child.child.item.key);
        // Assertions.assertEquals(21, bh1.last.child.child.child.next.item.key);
        // Assertions.assertEquals(17, bh1.last.child.child.next.item.key);
        // Assertions.assertEquals(18, bh1.last.child.child.next.next.item.key);
        // Assertions.assertEquals(19, bh1.last.child.child.next.next.child.item.key);
        // Assertions.assertEquals(2, bh1.last.child.next.item.key);
        // Assertions.assertEquals(3, bh1.last.child.next.next.item.key);
        // Assertions.assertEquals(4, bh1.last.child.next.next.child.item.key);
        // Assertions.assertEquals(5, bh1.last.child.next.next.next.item.key);
        // Assertions.assertEquals(7, bh1.last.child.next.next.next.child.item.key);
        // Assertions.assertEquals(8,
        // bh1.last.child.next.next.next.child.child.item.key);
        // Assertions.assertEquals(6,
        // bh1.last.child.next.next.next.child.next.item.key);

    }
}