package compilerLab1;

import java.util.Hashtable;
import java.util.Arrays;
import java.util.List;

public class DFA {
	static Hashtable<Integer, State> map = new Hashtable<Integer, State>();
	String goals;

	public DFA(String goals) {
		this.goals = goals;
	}

	public static DFA solvedfa(String string) { // The string i will get will be semicolon separated, each state
												// transition is
		// comma separated. The goals will be presented after the hashtag and will be
		// separated by commas
		// create a hashtable to insert the states in
		map.clear();
		String[] states_goals = splitString(string, "#"); // separate goals from states: P#S
		String[] getStates = splitString(states_goals[0], ";");// separate states from each other
		// get each state and create a State for it then push it in the array
		for (int i = 0; i < getStates.length; i++) {
			String[] ans = splitString(getStates[i], ","); // split on the comma
			State s = new State(ans[0], ans[1], ans[2]); // create the state
			map.put(Integer.parseInt(s.name), s); // put the state in the Hashtable
			// System.out.println(Integer.parseInt(s.name));
		}

		return new DFA(states_goals[1]);
	}

	public static boolean run(String s, String binaryString) {
		DFA d = solvedfa(s);

		String[] goals = splitString(d.goals, ","); // we got the goals'

		State currentState = map.get(0);
		String[] bString = splitString(binaryString, ","); // contains the test

		for (int i = 0; i < bString.length; i++) {

			if (bString[i].equals("1")) {

				currentState = map.get(Integer.parseInt(currentState.toOneState));

			} else {
				currentState = map.get(Integer.parseInt(currentState.toZeroState));
			}
		}
		List<String> list = Arrays.asList(goals);
		if (list.contains(currentState.name)) {
			return true;
		} else {
			return false;
		}
	}

	public static String[] splitString(String s, String operator) {
		String[] sections = s.split(operator);
		return sections;
	}

	public static void addToHashtable(State s, Hashtable<Integer, State> map) {
		map.put(Integer.parseInt(s.name), s);
	}

	public static void main(String[] args) {
		System.out.println(run("0,3,1;1,2,1;2,2,1;3,3,3#2", "0,1,0,0,1,0"));

	}
}
