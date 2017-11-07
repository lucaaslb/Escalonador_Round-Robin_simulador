package lista;

import escalonador.Processo;

public class Lista {

	private Node first, last;
	private int size, lastIndice;

	public Lista() {
		first = last = null;
		size = 0;
		lastIndice = 0;
	}

	public void insere(Processo p) {
		Node novo = new Node(p);
		if (first == null) {
			last = novo;
		} else {
			novo.Next = first;
		}
		first = novo;
		size++;
	}

	public void imprime() {
		Node aux = first;
		if (aux == null) {
			System.out.println("Lista Vazia");
		} else {
			while (aux != null) {
				System.out.print(aux.processo + "\n");
				aux = aux.Next;
			}
		}
	}

	public Processo localiza(int indice) {
		if (indice < 0 || indice > size) {
			System.out.println("Ã�ndice Ã© invÃ¡lido");
			return null;
		} else {
			Node aux = first;
			for (int i = 0; i < indice; i++) {
				aux = aux.Next;
			}
			return aux.processo;
		}
	}

	public int getSize() {
		return size;
	}

	public Processo verificaInicio(Lista list, int atual) {
		Node aux = first;

		while (aux != null) {
			if (aux.processo.getiStart() == atual) {
				//System.out.print(aux.processo);
				return aux.processo;
			}
			aux = aux.Next;
		}
		return null;
	}

	public int verificaDuracao() {
		Node aux = first;
		int duracaoTotal = 0;
		while (aux != null) {
			duracaoTotal += aux.processo.getiDuration();
			aux = aux.Next;
		}
		//System.out.println("duracao: " + duracaoTotal);
		return duracaoTotal;
	}

	public void remove(int indice) {
		Processo aux;
		if (indice < 0 || indice > size) {
			System.out.println("Ã�ndice Ã© invÃ¡lido");
		} else {
			Node node = first;
			for (int i = indice; i < this.size; i++) {
				aux = node.processo;
				node.Next = node;
			}

		}

	}
}
