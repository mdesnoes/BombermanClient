package com.projetBomberman.perceptron;

import java.util.Random;

public class Perceptron implements BinaryClassifier
{
	protected SparseVector parameters;
	protected int nb_iterations;
	protected double epsilon;
	private int taille;
	private int nb_actions;
	
	private Random randomno;
	
	public Perceptron(double epsilon, int nb_iterations, SparseVector param, int nb_actions)
	{
		this.epsilon=epsilon;
		this.nb_iterations=nb_iterations;
		this.parameters=param;		
		this.nb_actions = nb_actions;
		
		randomno = new Random();
	}
	
	
	public Perceptron(double epsilon, int taille, int nb_actions){
		
		this.epsilon=epsilon;
		this.nb_actions=nb_actions;
		this.taille = taille;
		
		randomno = new Random();
		parameters=new SparseVector(taille*taille*3*nb_actions+1);
		
		
		
		for(int i=0;i<taille*taille*3*nb_actions + 1;i++){
			parameters.setValue(i, randomno.nextGaussian() * epsilon);
		}	
	}
	
	public String toString(){
		return epsilon+"   "+parameters.toString();
	}
	
	
	@Override
	public double getScore(SparseVector v) 
	{

		return parameters.computeDOT(v);
	}

	
	@Override
	public void train(LabeledSet training_set) 
	{
		for(int i=0;i<nb_iterations ;i++){
			
	
			for (int j=0;j<training_set.size();j++){
				
				SparseVector v=training_set.getVector(j);
				double reel=training_set.getLabel(j);
				double predicted=getScore(v);
				double error=reel -predicted;	
				parameters.addVector(v,epsilon*error);
				
			}
		}
	}
	
	
	public void setNb_iteration(int val){
		nb_iterations=val;
	}
	
	public void bruiter(double standardDeviation){

	
		for (int i=0;i<parameters.size;i++){
			
			parameters.setValue(i, parameters.getValue(i)+randomno.nextGaussian()*standardDeviation);
			
		}
		
	}

	
	public void copy(SparseVector myParams){

		
		for (int i=0;i<parameters.size;i++){
			
			parameters.setValue(i, myParams.getValue(i));
			
		}
		
	}
	


}