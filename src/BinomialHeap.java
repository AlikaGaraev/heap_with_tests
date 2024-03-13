/**
 * username1 - ishayyem
 * id1       - 322868852
 * name1     - Ishay Yemini
 * username2 - alikagaraev
 * id2       - 323222141
 * name2     - Alika Garaev
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
import java.lang.Math;
public class BinomialHeap {
	public int size;
	public int treeNum;
	public HeapNode last;
	public HeapNode min;

	public BinomialHeap() {
		this.size = 0;
		this.treeNum = 0;
	}

	/**
	 * pre: key > 0
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 */
	public HeapItem insert(int key, String info) {
		if (this.last == null) {
			HeapNode newNode = new HeapNode(key, info);
			this.last = newNode;
			this.min = newNode;
			this.size = 1;
			this.treeNum = 1;
			newNode.next = newNode;
			return newNode.item;
		} else {
			BinomialHeap heap = new BinomialHeap();
			HeapItem item = heap.insert(key, info);
			this.meld(heap);
			return item;
		}
	}

	/**
	 * Delete the minimal item
	 */
	public void deleteMin() {
		if (this.size == 0) return;
		if (this.size == 1) {
			this.size = 0;
			this.treeNum = 0;
			this.last = null;
			this.min = null;
			return;
		}

		HeapNode oldMin = this.min;
		HeapNode prevMin = null;
		HeapNode newMin = this.min.next;
		HeapNode node = this.last;
		while (node.next != this.last) {      //finding the new min
			node = node.next;
			if (node.item.key < newMin.item.key && node != oldMin) {
				newMin = node;
			}
			if (node.next == oldMin) {
				prevMin = node;
			}
		}
		if (prevMin != null) prevMin.next = prevMin.next.next;   //skipping oldMin
		if (this.last == this.min) this.last = prevMin;

		this.treeNum -= 1;
		this.size -= 1;
		this.min = newMin;    //setting the new min

		if (oldMin.child != null) {
			node = oldMin.child;
			node.parent = null;
			BinomialHeap newHeap = new BinomialHeap();   //setting new binomial heap with the children of the old min
			newHeap.min = node;
			newHeap.last = node;
			newHeap.size = (int) Math.pow(2, node.rank);
			newHeap.treeNum = 1;
			while (node.next != oldMin.child) {
				node = node.next;
				node.parent = null;
				if (node.item.key < newHeap.min.item.key) {
					newHeap.min = node;
				}
				newHeap.size += Math.pow(2, node.rank);
				newHeap.treeNum += 1;
			}
			this.size -= newHeap.size;
			this.meld(newHeap);
		}

	}

	/**
	 * Return the minimal HeapItem
	 */
	public HeapItem findMin() {
		return min.item;
	}

	/**
	 * pre: 0 < diff < item.key
	 * Decrease the key of item by diff and fix the heap.
	 */
	public void decreaseKey(HeapItem item, int diff) {
		item.key -= diff;
		HeapNode node = item.node;
		while (node.parent != null && node.item.key <= node.parent.item.key) {
			node.item = node.parent.item;
			node.parent.item.node = node.parent;
			node.parent.item = item;
			item.node = node.parent;
			node = node.parent;
		}
		if (this.min.item.key >= item.key) {
			this.min = item.node;
		}
	}

	/**
	 * Delete the item from the heap.
	 */
	public void delete(HeapItem item) {
		this.decreaseKey(item, item.key);
		this.deleteMin();
	}

	/**
	 * Meld the heap with heap2
	 */
	public void meld(BinomialHeap heap2) {
		if (heap2.empty()) return;
		if (this.empty()) {
			this.last = heap2.last;
			this.min = heap2.min;
			this.size = heap2.size;
			this.treeNum = heap2.treeNum;
			return;
		};

		this.size += heap2.size();

		// Merge the two heaps
		if (heap2.size() == 1) {
			heap2.last.setNext(this.last.next);
			this.last.setNext(heap2.last);
		} else {
			HeapNode node1 = this.last.next;
			HeapNode node2 = heap2.last.next;
			if (this.last.next.rank > heap2.last.next.rank) {
				node1 = heap2.last.next;
				node2 = this.last.next;
			}
			if (this.last.rank > heap2.last.rank) this.last.next = node1;
			else heap2.last.next = node1;

			while (node1 != this.last && node1 != heap2.last) {
				if (node1.next.rank > node2.rank) {
					HeapNode oldNext = node1.next;
					node1.setNext(node2);
					node2 = oldNext;
				}
				node1 = node1.next;
			}
//			if (this.last.rank <= node1.rank) node
			node1.setNext(node2);
		}

		// Consolidate the trees if needed and find the new min
		HeapNode node = this.last.next;
		HeapNode oldNode = this.last;
		HeapNode min = node;
		this.treeNum = 1;
		while (node != this.last) {
			if (node.rank == node.next.rank && (node == node.next.next || node.rank != node.next.next.rank)) {
				node = mergeTrees(node, node.next);
				if (oldNode != node.child) oldNode.setNext(node);
				if (this.last == node.child) this.last = node;
				continue;
			}
			// Only go forward if we didn't merge!
			oldNode = node;
			node = node.next;
			this.treeNum++;
			if (node.item.key < min.item.key) min = node;
		}
		this.min = min;
	}

	HeapNode mergeTrees(HeapNode n1, HeapNode n2) {
		//if (n1.rank != n2.rank) return n2;
		if (n1.item.key < n2.item.key) {
			n1.next = n2.next;
			return mergeTrees(n2, n1);
		}
		if (n2.child != null) {
			n1.setNext(n2.child.next);
			n2.child.setNext(n1);
		} else n1.setNext(n1);
		n2.child = n1;
		n1.parent = n2;
		n2.rank++;
		return n2;
	}

	/**
	 * Return the number of elements in the heap
	 */
	public int size() {
		return this.size;
	}

	/**
	 * The method returns true if and only if the heap
	 * is empty.
	 */
	public boolean empty() {
		return this.size == 0;
	}

	/**
	 * Return the number of trees in the heap.
	 */
	public int numTrees() {
		return this.treeNum;
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 */
	public class HeapNode {
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;

		public HeapNode() {}

		public HeapNode(int key, String info) {
			this.item = new HeapItem(this, key, info);
		}

		public void setNext(HeapNode next) {
			this.next = next;
		}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 */
	public class HeapItem {
		public HeapNode node;
		public int key;
		public String info;

		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info = info;
		}
	}

	public void printHeap() {
		if (empty()) {
			System.out.println("Heap is empty");
			return;
		}
		System.out.println("Binomial Heap:");
		HeapNode currentRoot = last;
		HeapNode stopNode = last.next;
		boolean stop = false;

		do {
			System.out.println("Root: " + currentRoot.item.key);
			currentRoot = currentRoot.next;
			printTree(currentRoot, 0, currentRoot);
			if (currentRoot == stopNode) stop = true;
		} while (!stop);
	}

	private void printTree(HeapNode node, int depth, HeapNode initialRoot) {
		System.out.println("  ".repeat(Math.max(0, depth)) + node.item.key + " [" + node.rank + "]");
		if (node.child != null) {
			printTree(node.child.next, depth + 1, node.child.next);
		}
		if (node.next != initialRoot) {
			printTree(node.next, depth, initialRoot);
		}
	}
}
