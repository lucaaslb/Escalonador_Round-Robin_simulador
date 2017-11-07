/**
 *
 * @author lucas lacerda
 * 		twitter: @lucaaslb
 */
package escalonador;

import java.util.Arrays;

public class Processo {

    private String sName; // PID
    private int iDuration; // Duracao do processo
    private int iStart;  // Instante de chegada
    private int iCurrentDuration; // duracao atual (ap�s ser escalonado)
    private int iRunTime; // verificar o quanto do processo ja foi executado para facilitar no momneto de tratar operações de I/O
    private int[] viInOut; // opera��es de I/O 

    /* aux para calcular tempo medio de espera */
    private double iStandBy; // recebe a diferença do iWaiting com o tempo atual para verificar o quanto ficou na espera, sempre incrementando para 
    // podermos verificar o tempo de espera total do processo
    private double iWaiting; // recebe o valor do tempo atual quando posta na fila

    public Processo(String sName, int iDuration, int iStart, int iSizeInOut) {
        this.sName = sName;
        this.iDuration = iDuration;
        this.iStart = iStart;
        this.viInOut = new int[iSizeInOut];
        this.iCurrentDuration = iDuration;
        this.iRunTime = 0;
        this.iWaiting = iStart;
    }

    public Processo() {

    }

    public String getsName() {
        return sName;
    }

    public int getiDuration() {
        return iDuration;
    }

    public int getiStart() {
        return iStart;
    }

    public int getiCurrentDuration() {
        return iCurrentDuration;
    }

    //sempre que chamar setiCurrenteDuration, será decrementado 1 do valor 
    public void setiCurrentDuration() {
        this.iCurrentDuration--;
    }

    public int getiRunTime() {
        return iRunTime;
    }

    //sempre que chamar setiRunTime, será incrementado 1 do valor 
    public void setiRunTime() {
        this.iRunTime++;
    }

    public int[] getViInOut() {
        return viInOut;
    }

    /* Preencher vetor de operações I/O com informações do arquivo*/
    public void preencheIO(int indice, int value) {
        viInOut[indice] = value;
    }

    public int iverificaIO(int iTempoEmExecucao) {

        for (int x = 0; x < this.viInOut.length; x++) {
            if (viInOut[x] == iTempoEmExecucao) {
                return viInOut[x];
            }
        }
        return -1;
    }

    public double getiStandBy() {
        return iStandBy;
    }

    // calcula de tempo de espera
    public void setiStandBy(int iTempoAtual) {
        this.iStandBy = (iTempoAtual - iWaiting) + this.iStandBy;
    }

    public double getiWaiting() {
        return iWaiting;
    }

    /* Instante que entrou na fila */
    public void setiWaiting(int iTempoAtual) {
        this.iWaiting = iTempoAtual;
    }

    @Override
    public String toString() {
        return "Processo [sName: " + sName + ", iDuration: " + iDuration + ", iStart: " + iStart + ", iCurrentDuration: "
                + iCurrentDuration + ", viInOut: " + Arrays.toString(viInOut) + "]";
    }

}
