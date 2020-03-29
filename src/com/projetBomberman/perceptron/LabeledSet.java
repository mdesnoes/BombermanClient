package com.projetBomberman.perceptron;

import java.util.ArrayList;

public class LabeledSet {
	protected ArrayList<SparseVector> vectors;
	protected ArrayList<Double> labels;
	protected int size_vectors;
	
	public LabeledSet(int size_vectors)
	{
		vectors=new ArrayList<SparseVector>();
		labels=new ArrayList<Double>();
		this.size_vectors=size_vectors;
	}
	
	/**
 	* Renvoie la taille des vecteurs
 	*/
	public int getSizeVectors()
	{
		return(size_vectors);
	}
	
	public void addExample(SparseVector v, Double l)
	{
		vectors.add(v);
		labels.add(l);
	}
	
	public int size() 
	{
		return(vectors.size());
	}
	public SparseVector getVector(int i)
	{
		return(vectors.get(i));		
	}
	
	public Double getLabel(int i)
	{
		return(labels.get(i));
	}
	 
}
