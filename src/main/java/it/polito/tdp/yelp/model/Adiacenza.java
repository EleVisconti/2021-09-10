package it.polito.tdp.yelp.model;

import java.util.Objects;

public class Adiacenza implements Comparable<Adiacenza> {
 Business b1;
 Business b2;
 double peso;
public Adiacenza(Business b1, Business b2, double peso) {
	super();
	this.b1 = b1;
	this.b2 = b2;
	this.peso = peso;
}
@Override
public int hashCode() {
	return Objects.hash(b1, b2, peso);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Adiacenza other = (Adiacenza) obj;
	return Objects.equals(b1, other.b1) && Objects.equals(b2, other.b2)
			&& Double.doubleToLongBits(peso) == Double.doubleToLongBits(other.peso);
}
public Business getB1() {
	return b1;
}
public void setB1(Business b1) {
	this.b1 = b1;
}
public Business getB2() {
	return b2;
}
public void setB2(Business b2) {
	this.b2 = b2;
}
public double getPeso() {
	return peso;
}
public void setPeso(double peso) {
	this.peso = peso;
}
@Override
public int compareTo(Adiacenza o) {
	// TODO Auto-generated method stub
	if(this.getPeso()>o.getPeso())
		return 1;
	else
	 return -1;
}
 
 
}
