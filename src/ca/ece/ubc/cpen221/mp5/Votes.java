package ca.ece.ubc.cpen221.mp5;

public class Votes {
	
	/*
	 * RepInvariant:
	 * 		total = funny + useful + cool;
	 * 		funny, useful, cool, total >= 0;
	 */
	
	//all these fields are immutable, hence they are safe to be shared
	public final long funny;
	public final long useful;
	public final long cool;
	public final long total;
	
	public Votes(long funny, long useful, long cool) {
		this.funny = funny;
		this.useful = useful;
		this.cool = cool;
		this.total = funny + useful + cool;
	}
}
