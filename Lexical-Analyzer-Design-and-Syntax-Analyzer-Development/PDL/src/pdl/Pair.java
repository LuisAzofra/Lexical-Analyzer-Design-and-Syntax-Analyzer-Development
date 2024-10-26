package pdl;
import java.util.*;

public class Pair<A,B>{
	private A left;
	private B rigth;

	public Pair(A left, B rigth){
		this.left = left;
		this.rigth = rigth;
	}
	public A getLeft(){
		return this.left;
	}
	public B getRigth(){
		return this.rigth;
	}
	public void setLeft(A left){
		this.left = left;
	}
	public void setRigth(B rigth){
		this.rigth = rigth;
	}
}