package compilerLab1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class NFAtoDFA {

	public static String[] splitString(String s, String operator) {
		String[] sections = s.split(operator);
		return sections;
	}

	public static Set<Character> getUnique(String string) {
		Set<Character> characters = new TreeSet<>();
		for (char c : string.toCharArray()) {
			if (Character.isDigit(c))
				characters.add(c);

		}

		return characters;
	}

public static String getEpsilons(Hashtable<Integer, nfaState> ht) {
	String startName="";
	LinkedList<String> toBeExecuted = new LinkedList<String>();
	Hashtable<String, String> epsilons = new Hashtable<String, String>();
	if(ht.get(0).toEpsilon.length()>0) {		
		toBeExecuted.add(ht.get(0).toEpsilon);
		while(!toBeExecuted.isEmpty()) {
			String current = toBeExecuted.remove();
			String [] currentString = splitString(current,",");
			for (int i=0;i<currentString.length;i++) {
				if(!epsilons.containsKey(currentString[i])) {
					epsilons.put(currentString[i], currentString[i]);
					if(startName.equals(""))
					startName +=currentString[i];
					else
					startName+=","+currentString[i];
					if(ht.get(Integer.parseInt(currentString[i])).toEpsilon.length()>0) {
						toBeExecuted.add(ht.get(Integer.parseInt(currentString[i])).toEpsilon);
					}
				}
			}
			
		}
	}
	else {
		startName="";
		
	}
	return sortStates(startName);
}

	public static String getStartState(String nfa) {
		Hashtable<Integer, nfaState> ht = breakNFA(nfa);
		String startName;
		if (ht.get(0).toEpsilon.length() > 0) {
			startName = ht.get(0).name + "," + getEpsilons(ht);

		} else {
			startName = ht.get(0).name;
		}
		startName = sortStates(startName);
		return startName;
	}

	public static Hashtable<String, State> nfa_to_dfa(String nfa) {
		LinkedList<String> toBeExecuted = new LinkedList<String>();
		String startName;
		Hashtable<Integer, nfaState> ht = breakNFA(nfa);
		Hashtable<String, State> DFAStates = new Hashtable<String, State>();
		DFAStates.clear();
		if (ht.get(0).toEpsilon.length() > 0) {
			//startName = ht.get(0).name + "," + ht.get(0).toEpsilon;
			startName = ht.get(0).name + "," + getEpsilons(ht);

		} else {
			startName = ht.get(0).name;
		}
		startName = sortStates(startName);
		System.out.println(startName);
		toBeExecuted.add(startName);
		while (!toBeExecuted.isEmpty()) {
			String name = toBeExecuted.getFirst();
			if (!DFAStates.containsKey(name) && !(name.equals("dead")) && !(name.equals(""))) {
				toBeExecuted.removeFirst();
				String[] names = splitString(name, ",");
				String nextState0 = "";
				String nextState1 = "";

				for (int n = 0; n < names.length; n++) {
					nfaState currentNFA = ht.get(Integer.parseInt(names[n]));
					String zeros = currentNFA.toZeroState;
					String ones = currentNFA.toOneState;
					String[] checkOnes = splitString(ones, ",");
					String[] checkZeros = splitString(zeros, ",");
					for (int i = 0; i < checkOnes.length; i++) {
						if (checkOnes[i].length() > 0)
							if (!(ht.get(Integer.parseInt(checkOnes[i])).toEpsilon.equals(""))) {
								ones += "," + ht.get(Integer.parseInt(checkOnes[i])).toEpsilon;
							}
					}

					if (n > 0)
						nextState1 += "," + ones;
					else
						nextState1 += ones;
					////////////////////////////////////////////////////// DO THE SAME FOR ZEROS
					////////////////////////////////////////////////////// /////////////////////////////////////////////////////
					for (int i = 0; i < checkZeros.length; i++) {
						if (checkZeros[i].length() > 0) {
							if (ht.get(Integer.parseInt(checkZeros[i])).toEpsilon.length() > 0) {
								zeros += "," + ht.get(Integer.parseInt(checkZeros[i])).toEpsilon;
							}
						}
					}

					if (n > 0)
						nextState0 += "," + zeros;
					else
						nextState0 += zeros;

				}

				nextState1 = sortStates(nextState1);
				if (nextState1.equals("")) {
					nextState1 = "dead";
				}
				nextState0 = sortStates(nextState0);
				if (nextState0.equals("")) {
					nextState0 = "dead";

				}

				// check if the hashmap doesnt contain the next states perform the operations on
				// them then add
				DFAStates.put(name, new State(name, nextState0, nextState1));
				if ((nextState0.equals("dead") || nextState1.equals("dead")) && !(DFAStates.containsKey("dead"))) {
					DFAStates.put("dead", new State("dead", "dead", "dead"));
				}
				if (!(DFAStates.containsKey(nextState0))) {
					toBeExecuted.add(nextState0);
				}
				if (!(DFAStates.containsKey(nextState1))) {
					toBeExecuted.add(nextState1);
				}

			} else {

				toBeExecuted.removeFirst();
			}

		}
		//////////

	Set<String> keys = DFAStates.keySet();
		System.out.println("Name/ID:");
		for (String key : keys) {
			System.out.println("Value of " + key + " is: " + DFAStates.get(key).name);
		}
		System.out.println("==========");
		System.out.println("Zeros:");
		for (String key : keys) {
			System.out.println("Value of " + key + " is: " + DFAStates.get(key).toZeroState);
		}
		System.out.println("==========");
		System.out.println("Ones:");
		for (String key : keys) {
			System.out.println("Value of " + key + " is: " + DFAStates.get(key).toOneState);
		}
		// //////////

		return DFAStates;

	}

	public static String sortStates(String s) { // sort and arrange string
		HashSet<String> removeDuplicates = new HashSet<String>(Arrays.asList(s.split(",")));
		removeDuplicates.removeAll(Collections.singleton(""));
		s = String.join(",", removeDuplicates);
		String[] x = splitString(s, ",");
		Arrays.sort(x, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			}
		});

		s = String.join(",", x);
		return s;
	}

	public static boolean runNFA(String nfa, String test) {
		String[] separated1 = splitString(nfa, "#");
		String[] goals = splitString(separated1[separated1.length - 1], ",");
		Hashtable<String, State> ht = nfa_to_dfa(nfa);
		String startKey = getStartState(nfa);
		State currentState = ht.get(startKey);
		String[] bString = splitString(test, ","); // contains the test
		for (int i = 0; i < bString.length; i++) {

			if (bString[i].equals("1")) {
				currentState = ht.get(currentState.toOneState);
				if (currentState == null) {
					return false;
				}

			} else {

				currentState = ht.get(currentState.toZeroState);
				if (currentState == null) {
					return false;
				}
			}
		}
		String[] namesReached = splitString(currentState.name, ",");
		List<String> list = Arrays.asList(goals);
		for (int i = 0; i < namesReached.length; i++) {
			if (list.contains(namesReached[i])) {
				return true;
			}
		}
		return false;
	}

	public static Hashtable<Integer, nfaState> breakNFA(String nfaString) {
		Hashtable<Integer, nfaState> NFAtableTransition = new Hashtable<Integer, nfaState>();
		String[] separated1 = splitString(nfaString, "#");
		String[] toZero = splitString(separated1[0], ";"); // states which have zero transition
															// current,destination;current2,destination2..
		String[] toOnes = splitString(separated1[1], ";"); // states which have one transition
															// current,destination;current2,destination2..
		String[] toEpsilon = splitString(separated1[2], ";"); // states which have epsilon transition
		if (!separated1[0].equals("")) // current,destination;current2,destination2..
			for (int i = 0; i < toZero.length; i++) {
				String[] state_trans = splitString(toZero[i], ",");

				if (NFAtableTransition.containsKey(Integer.parseInt(state_trans[0]))) {
					if (NFAtableTransition.get(Integer.parseInt(state_trans[0])).toZeroState.equals("")) {
						NFAtableTransition.get(Integer.parseInt(state_trans[0])).toZeroState += state_trans[1];
					} else {
						NFAtableTransition.get(Integer.parseInt(state_trans[0])).toZeroState += "," + state_trans[1];
					}
				} else {
					nfaState nfa = new nfaState(state_trans[0], state_trans[1], "", "");
					NFAtableTransition.put(Integer.parseInt(state_trans[0]), nfa);
				}
			}
		if (!separated1[1].equals(""))
			for (int i = 0; i < toOnes.length; i++) {
				String[] state_trans = splitString(toOnes[i], ",");

				if (NFAtableTransition.containsKey(Integer.parseInt(state_trans[0]))) {
					if (NFAtableTransition.get(Integer.parseInt(state_trans[0])).toOneState.equals("")) {
						NFAtableTransition.get(Integer.parseInt(state_trans[0])).toOneState += state_trans[1];
					} else {
						NFAtableTransition.get(Integer.parseInt(state_trans[0])).toOneState += "," + state_trans[1];
					}
				} else {
					nfaState nfa = new nfaState(state_trans[0], "", state_trans[1], "");
					NFAtableTransition.put(Integer.parseInt(state_trans[0]), nfa);
				}
			}

		if (!separated1[2].equals(""))
			for (int i = 0; i < toEpsilon.length; i++) {

				String[] state_trans = splitString(toEpsilon[i], ",");

				if (NFAtableTransition.containsKey(Integer.parseInt(state_trans[0]))) {
					if (NFAtableTransition.get(Integer.parseInt(state_trans[0])).toEpsilon.equals("")) {
						NFAtableTransition.get(Integer.parseInt(state_trans[0])).toEpsilon += state_trans[1];
					} else {
						NFAtableTransition.get(Integer.parseInt(state_trans[0])).toEpsilon += "," + state_trans[1];
					}
				} else {
					nfaState nfa = new nfaState(state_trans[0], "", "", state_trans[1]);
					NFAtableTransition.put(Integer.parseInt(state_trans[0]), nfa);
				}
			}

		Set<Character> schar = getUnique("0,1;0,2;2,1#2,2;2,1#0,1#1");
		Iterator iter = schar.iterator();
		while (iter.hasNext()) {
			int x = Integer.parseInt("" + iter.next());
			if (!NFAtableTransition.containsKey(x)) {
				nfaState nfa = new nfaState(x + "", "", "", "");
				NFAtableTransition.put(x, nfa);
			}

		}
		
/*		  Set<Integer> keys = NFAtableTransition.keySet();
		  System.out.println("Name/ID:"); for (Integer key : keys) {
		  System.out.println("Value of " + key + " is: " +
		  NFAtableTransition.get(key).name); } System.out.println("==========");
		  System.out.println("Zeros:"); for (Integer key : keys) {
		  System.out.println("Value of " + key + " is: " +
		  NFAtableTransition.get(key).toZeroState); } System.out.println("==========");
		  System.out.println("Ones:"); for (Integer key : keys) {
		  System.out.println("Value of " + key + " is: " +
		  NFAtableTransition.get(key).toOneState); } System.out.println("==========");
		 System.out.println("Epsilon:"); for (Integer key : keys) {
		  System.out.println("Value of " + key + " is: " +
		  NFAtableTransition.get(key).toEpsilon); }*/
		 
		return NFAtableTransition;
	}

	public static void main(String[] args) {
		//boolean result = runNFA("0,0;1,2;3,3#0,0;0,1;2,3;3,3#1,2#3", "0,1,0,1");
		// //answer:
		boolean result1 = runNFA("0,2;1,0;2,1#2,2;2,1#0,1#1", "0"); //answer:
		// boolean result2 = runNFA("0,0;0,1#0,1;1,0##0", "1,0"); //answer:
		 //boolean result3 = runNFA("0,1;0,2;2,1#2,2;2,1#0,1#1","0,0,0");
		// boolean result4 = runNFA("0,1;2,2#1,2;2,2#0,3;3,2#2","");
		// System.out.println(result2 + " " + result1 + " " + result);
	    System.out.println(result1);
		//System.out.println(sortStates("1,2,3,2,1,"));
	

	}
}
