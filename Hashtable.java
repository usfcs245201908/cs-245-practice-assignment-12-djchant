import java.util.*;
import java.lang.Math;

public class Hashtable {
	ArrayList<HashNode> bucket;
	float LOAD_THRESHOLD = 0.5f;
	int entries = 0;
	int numBuckets;

	public Hashtable() {
		numBuckets = 2027;
		bucket = new ArrayList<HashNode>(numBuckets);

		for(int i = 0; i < numBuckets; i++) {
			bucket.add(null);
		}
	}

	private int getHash(String key) {
		return(Math.abs(key.hashCode() % bucket.size()));
	}

	//returns a value that matches with the key
	public String get(String key) {
		HashNode head = bucket.get(getHash(key));

		while(head != null) {
			if(head.key.equals(key)) {
				return head.value;
			} else {
				head = head.next;
			}
		}
		return null;
	}

	//inserts key and value pair
	public void put(String key, String value) {
		HashNode head = bucket.get(getHash(key));

		//if bucket is empty, set the key,value pair as the first node
		if(head == null) {
			bucket.set(getHash(key), new HashNode(key, value));
		//bucket is not empty, look to see if there's existing matching key
		//replace value if key exists, create new node if matching key cannot be found
		} else {
			while(head != null) {
				if(head.key.equals(key)) {
					head.value = value;
					return;
				} else {
					head = head.next;
				}
			}

			HashNode node = new HashNode(key, value);
			node.next = bucket.get(getHash(key));
			bucket.set(getHash(key), node);
		}

		entries++;
		if(entries * 1.0 / bucket.size() >= LOAD_THRESHOLD) {
			//increase number of buckets
			//rehash each item

			ArrayList<HashNode> temp = bucket;

			bucket = new ArrayList<HashNode>(numBuckets * 2);
			entries = 0;
			numBuckets *= 2;

			for(int i = 0; i < numBuckets; i++) {
				bucket.add(null);
			}

			for(int i = 0; i < temp.size(); i++) {
				HashNode otherHead = temp.get(i);

				while(otherHead != null) {
					String key2 = otherHead.key;
					String value2 = otherHead.value;

					put(key2, value2);
					otherHead = otherHead.next;
				}
			}
		}
	}

	//searches a bucket to see if the key can be found
	public boolean containsKey(String key) {
		HashNode head = bucket.get(getHash(key));

		//bucket is empty
		if(head == null) {
			return false;
		//bucket is not empty
		} else {
			while(head != null) {
				if(head.key.equals(key)) {
					return true;
				}
			}
		}

		return false;
	}

	//removes the node that matches the key and returns the value of that key
	public String remove(String key) throws Exception {
		HashNode head = bucket.get(getHash(key));
		String temp = ""; 


		if(head != null) {
			//if key is the first node
			if(head.key.equals(key)) {
				temp = head.value;
				bucket.set(getHash(key), head.next);
			//key might be any node after the first node
			} else {
				HashNode prev = head;
				HashNode current = null;
				while(prev.next != null) {
					current = prev.next;
					if(current.key == key) {
						temp = current.value;
						prev.next = current.next;
					}
				}
			}
			entries--;
			return temp;
		} else {
			//throws exception if bucket is empty
			throw new Exception("key not found");
		}
	}
}