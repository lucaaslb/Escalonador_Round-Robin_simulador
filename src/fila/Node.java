/**
 *
 * @author lucas lacerda 
 * 		twitter: @lucaaslb
 */

package fila;

import escalonador.Processo;

public class Node {

	public Processo processo;
	public Node next;

	public Node(Processo p) {
		processo = p;
		next = null;
	}

	public String displayNo() {
		return processo.getsName() + "(" + processo.getiCurrentDuration()+ ")";
	}

}
