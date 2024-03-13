/**
 * username1 - ishayyem
 * id1       - 322868852
 * name1     - Ishay Yemini
 * username2 - alikagaraev
 * id2       - 323222141
 * name2     - Alika Garaev
 * BinomialHeap
 * <p>
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */

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

		BinomialHeap newHeap = new BinomialHeap();
		if (this.min.child != null) {
			// setting new binomial heap with the children of the old min
			HeapNode node = this.min.child;
			newHeap.min = node;
			newHeap.last = node;
			do {
				node.parent = null;
				if (node.item.key < newHeap.min.item.key) newHeap.min = node;
				newHeap.size += (int) Math.pow(2, node.rank);
				newHeap.treeNum += 1;
				node = node.next;
			} while (node != this.min.child);
		}

		if (this.treeNum == 1) { // We don't need to meld anything
			this.size = newHeap.size;
			this.treeNum = newHeap.treeNum;
			this.last = newHeap.last;
			this.min = newHeap.min;
		} else {
			HeapNode oldMin = this.min;
			HeapNode beforeMin = null;
			HeapNode newMin = this.min.next;
			HeapNode node = this.last;
			do {   //finding the new min
				node = node.next;
				if (node.item.key < newMin.item.key && node != oldMin) newMin = node;
				if (node.next == oldMin) beforeMin = node;
			} while (node != this.last);
			if (beforeMin != null) beforeMin.next = beforeMin.next.next;   //skipping oldMin
			if (this.last == this.min) this.last = beforeMin;

			this.size -= 1 + newHeap.size;
			this.treeNum -= 1;
			this.min = newMin;
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
		if (this.min.item.key >= item.key) this.min = item.node;
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
		}

		this.size += heap2.size();

		// Merge the two heaps
		HeapNode node1 = this.last.next;
		HeapNode node2 = heap2.last.next;
		if (node1.rank > node2.rank) {
			HeapNode oldNext = node2;
			node2 = node1;
			node1 = oldNext;
		}

		HeapNode first = node1;
		HeapNode last = this.last;
		if (heap2.last.rank >= last.rank) last = heap2.last;

		this.last.next = null;
		heap2.last.next = null;

		while (node1.rank <= node2.rank && node1.next != null) {
			if (node1.next.rank > node2.rank) {
				HeapNode oldNext = node1.next;
				node1.next = node2;
				node2 = oldNext;
			}
			node1 = node1.next;
		}
		if (node1 == last && node2.next == null) last = node2;
		if (node2 != first) node1.next = node2;

		this.last = last;
		this.last.next = first;

		// Consolidate the trees if needed and find the new min
		HeapNode node = this.last.next;
		HeapNode oldNode = this.last;
		HeapNode min = node;
		this.treeNum = 1;
		while (node != this.last) {
			boolean isNextSameRank = node.rank == node.next.rank;
			boolean isNextNextDiffRank = node.next == this.last || node.rank != node.next.next.rank;
			if (isNextSameRank && isNextNextDiffRank) {
				node = mergeTrees(node, node.next);
				if (oldNode != node.child) oldNode.next = node;
				if (this.last == node.child) this.last = node;
				if (node.item.key < min.item.key) min = node;
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
			n1.next = n2.child.next;
			n2.child.next = n1;
		} else n1.next = n1;
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

		public HeapNode() {
		}

		public HeapNode(int key, String info) {
			this.item = new HeapItem(this, key, info);
		}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 */
	public class HeapItem {
		public HeapNode node;
		public int key;
		public String info;

		public HeapItem() {
		}

		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info = info;
		}
	}
}
