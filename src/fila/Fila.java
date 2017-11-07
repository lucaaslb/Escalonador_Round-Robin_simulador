/**
 *
 * @author lucas lacerda 
 * 		twitter: @lucaaslb
 */

package fila;
import escalonador.Processo;

public class Fila {


    private Node first, last;
    private int iSize;

    public Fila() {
        first = null;      //Fila vazia
        last = null;
        iSize = 0;
    }

    public boolean isEmpty() {
        // true se a Fila est� vazia
        return (first == null);
    }

    public void enqueue(Processo p) {
        Node newNode = new Node(p);
        if (first == null) {
            first = newNode;
            last = first;
        } else {
            last.next = newNode;
            last = newNode;
        }
        iSize++;

    }

    public void dequeue() {      // remove do inicio da fila                         
        if (!isEmpty()) { //se estiver vazia retorna -1

            Node temp = first;
            // move o topo para o prox n�
            first = first.next;
            if (first == null) {
                last = null;
            }
            iSize--;
        }

    }

    public String display() {
        if (isEmpty()) {
            return "Não há processos na fila";
        } else {
            Node atual = first;            // do inicio
            while (atual != null) {      // at� o final
                // exibe a informa��o do n�
               return atual.displayNo();
                // move para o proximo n�                  
            }         
        }
         return "Não há processos na fila";
    }

    // utilizado para retornar o primeiro da fila
    public Processo next() {
        Node temp = first;
        if (first == null) {
            last = null;
        }
        return temp.processo;
    }

    //retorna o tamanho da fila 
    public int getSize() {
        return iSize;
    }

}
