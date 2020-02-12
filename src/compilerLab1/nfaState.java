package compilerLab1;

public class nfaState {
	String name;
	String toZeroState;
	String toOneState;
	String toEpsilon;
	public nfaState(String name, String toZeroState, String toOneState, String toEpsilon) {
		this.name = name;
		this.toZeroState = toZeroState;
		this.toOneState = toOneState;
		this.toEpsilon=toEpsilon;
	}
}
