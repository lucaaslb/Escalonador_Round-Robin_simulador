/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lista;

import escalonador.Processo;

/**
 *
 * @author lab202ap2
 */
public class Node {

    public Processo processo;
    public Node Next;

    public Node(Processo p) {
        processo = p;
        Next = null;
    }
}

