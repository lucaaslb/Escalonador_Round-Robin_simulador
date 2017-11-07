/**
 *
 * @author lucas lacerda
 * 		twitter: @lucaaslb
 */
package escalonador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import fila.Fila;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JOptionPane;
import lista.Lista;

public class Manager {

    private Lista list = new Lista();

    /*
	 * Metodo para realizar a leitura do arquivo e criar objetos do tipo
	 * Processo com as informa��es obtidas
     */
    public void readFile() {

        Processo processo;
        // String fileName = "processos.txt";
        String sLine, sName, sDuration, sStart;

        JFileChooser jf = new JFileChooser();

        // filtro para apresentar somente arquivos de texto
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        jf.setFileFilter(filter);

        int returnVal = jf.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileName = jf.getSelectedFile();

            try {
                FileReader file = new FileReader(fileName);
                BufferedReader bufReader = new BufferedReader(file);

                sLine = bufReader.readLine();

                while (sLine != null) {

                    // transforma a linha lida em um vetor separando as
                    // informacoes por " " (espa�o)
                    String[] vsLine = sLine.split("\\s");

                    sName = vsLine[0]; // index 1 = PID
                    sDuration = vsLine[1]; // index 2 =  Dura��o do processo
                    sStart = vsLine[2]; // index 3 =  instante de chegada do processo

                    // se o vetor da linha possir tamanho maior que 3 significa
                    // que o processo possui oparacoes de I/O
                    // desse modo entramos na condicao e criamos um objeto
                    // Processo com informacoes de I/O
                    if (vsLine.length > 3) {

                        String[] viInOut = vsLine[3].split(",");

                        processo = new Processo(sName, Integer.parseInt(sDuration), Integer.parseInt(sStart),
                                viInOut.length);

                        for (int i = 0; i < viInOut.length; i++) {
                            processo.preencheIO(i, Integer.parseInt(viInOut[i]));
                        }
                        //inserindo processo na lista
                        list.insere(processo);

                        //          System.out.println(processo.toString());
                    } else {
                        // se o processo nao possuir operacoes de I/O criamos um
                        // objeto com essa informacao nula
                        processo = new Processo(sName, Integer.parseInt(sDuration), Integer.parseInt(sStart), 0);

                        //inserindo processo na lista
                        list.insere(processo);
                        //           System.out.println(processo.toString());
                    }
                    sLine = bufReader.readLine();
                }

                file.close();

                //Obter valor do quantum
                try {
                    int quantum = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o valor do Quantum", "Round-Robin quantum:", 2));

                    escalonar(quantum);
                } catch (NumberFormatException exc) {
                    System.out.println("Somente numeros");

                    System.exit(0);
                } catch (HeadlessException exc) {
                    System.exit(0);
                }

            } catch (FileNotFoundException exc) {
                System.out.println("Arquivo " + fileName + " não encontrado");
            } catch (IOException exc) {
                exc.getStackTrace();
            }
        }
    }

    //Cria arquivo de saida com o resultado da simulação de execução dos processos
    public void writeFile(String sLine) {

        File file = new File("simulacao.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(sLine);
            writer.flush();
            //Fechando conexão e escrita do arquivo.
            writer.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    //metodo de escalonamento, recebe o valor do quantum
    public void escalonar(int quantum) {
        Processo processoEmExecucao = new Processo();

        int iTempoAtual = 0, iTempoEmExecucao = 0;
        double iTempoEmEspera = 0;
        boolean bEmExecucao = false;
        int iQtdProcessos = list.getSize(); //armazena a quantidade de processos da lista para poder calcular media de espera
        Fila queue = new Fila();

        // String que ira obter informações da execução para escrever no arquivo de saida
        String sLine = "*******************************"
                + "\n*** ESCALONADOR ROUND-ROBIN ***"
                + "\n*******************************"
                + "\n***** INICIANDO SIMULAÇÃO *****"
                + "\n*******************************\n";

        sLine += "*******************************\n"
                + "QUANTUM: " + quantum + "ms"
                + "\n*******************************";

        //lopping para executar o Tempo Atual até o tempo de duração total de todos processos, obtido pelo metodo verificaDuracao() da lista de processos
        for (iTempoAtual = 0; iTempoAtual <= list.verificaDuracao(); iTempoAtual++) {

            sLine += "\n\n*********** TEMPO " + iTempoAtual + " ***********";

            //condição para verificar tempo chegada do processo, se o retorno do metodo for diferente de null, significa que um processo
            // esta chegando no tempo atual e ele é adicionado na fila 
            if (list.verificaInicio(list, iTempoAtual) != null) {
                queue.enqueue(list.verificaInicio(list, iTempoAtual));
                sLine += "\n#[evento] CHEGADA: " + "<" + list.verificaInicio(list, iTempoAtual).getsName() + ">";
            }

            // primeira verificação, não existe processo sendo executado e ele será posto em execução para inicio.
            // tratando de diminuir valor do atributo iCurrentDuration para verificar o quanto falta para ser executado
            if (!bEmExecucao) {
                if (!queue.isEmpty()) {
                    processoEmExecucao = queue.next();
                    queue.dequeue();
                    processoEmExecucao.setiStandBy(iTempoAtual);
                    sLine += "\nFILA: " + queue.display() + "\nCPU: " + processoEmExecucao.getsName() + "(" + processoEmExecucao.getiCurrentDuration() + ")";

                    processoEmExecucao.setiCurrentDuration();
                    processoEmExecucao.setiRunTime();
                    bEmExecucao = true;
                    iTempoEmExecucao = 1;
                }

            } else {
                /*
                se ja existir um processo sendo executado, é necessario seguir nas verificações para tratar o escalonador
                 */

                //se o tempo restante para execução for igual a zero significa que o processo finalizou execução e podemos encerrar. 
                //se existir processo na fila colocamos em execução, se não, encerramos o escalonador
                if (processoEmExecucao.getiCurrentDuration() == 0) {
                    iTempoEmEspera += processoEmExecucao.getiStandBy(); //variavel armazena o tempo de espera de todos processos para calcular media 
                    sLine += "\n#[evento] ENCERRANDO <" + processoEmExecucao.getsName() + ">" + " - T. ESPERA: " + processoEmExecucao.getiStandBy() + "ms";

                    if (!queue.isEmpty()) {

                        processoEmExecucao = queue.next();
                        queue.dequeue();
                        processoEmExecucao.setiStandBy(iTempoAtual);
                        sLine += "\nFILA: " + queue.display() + "\nCPU: " + processoEmExecucao.getsName() + "(" + processoEmExecucao.getiCurrentDuration() + ")";
                        processoEmExecucao.setiCurrentDuration();
                        processoEmExecucao.setiRunTime();
                        bEmExecucao = true;
                        iTempoEmExecucao = 1;

                    } else {
                        sLine += "\nFILA: " + queue.display();
                        sLine += "\nACABARAM OS PROCESSOS";
                    }

                    //verificar se o tempo atual de execução do processo atingiu o valor do quantum para fazer a troca de contexto 
                } else if (iTempoEmExecucao == quantum) {

                    sLine += "\n#[evento] FIM DO QUANTUM <" + processoEmExecucao.getsName() + ">";
                    // e se ainda existir tempo para executar, o processo é enviado novamente a fila, salvando o tempo que entrou na fila para calculo de espera
                    if (processoEmExecucao.getiCurrentDuration() > 0) {

                        processoEmExecucao.setiWaiting(iTempoAtual);
                        queue.enqueue(processoEmExecucao);

                        processoEmExecucao = queue.next();
                        queue.dequeue();
                        //quando um processo é removido da fila e posto em execucao faz o calculo da diferença para saber o tempo de espera. 
                        processoEmExecucao.setiStandBy(iTempoAtual);

                        sLine += "\nFILA: " + queue.display() + "\nCPU: " + processoEmExecucao.getsName() + "(" + processoEmExecucao.getiCurrentDuration() + ")";
                        processoEmExecucao.setiCurrentDuration();
                        processoEmExecucao.setiRunTime();
                        bEmExecucao = true; // para não cair novamente no primeiro IF 
                        iTempoEmExecucao = 1;
                    }
                    //verifica se existe operações de I/O no tempo atual, passando para o metodo iverificaIO o valor getiRunTime que retorna o tempo
                    // de execucao que se encontra o processo atual 
                } else if (processoEmExecucao.iverificaIO(processoEmExecucao.getiRunTime()) > -1 && processoEmExecucao.getiCurrentDuration() > 0) {
                    sLine += "\n#[evento] OPERACAO I/O <" + processoEmExecucao.getsName() + ">";
                    processoEmExecucao.setiWaiting(iTempoAtual);
                    queue.enqueue(processoEmExecucao);
                    processoEmExecucao = queue.next();
                    queue.dequeue();
                    processoEmExecucao.setiStandBy(iTempoAtual);
                    sLine += "\nFILA: " + queue.display() + "\nCPU: " + processoEmExecucao.getsName() + "(" + processoEmExecucao.getiCurrentDuration() + ")";
                    processoEmExecucao.setiCurrentDuration();
                    processoEmExecucao.setiRunTime();
                    bEmExecucao = true;
                    iTempoEmExecucao = 1;

                } //se nao entrar em nenhuma das condicoes anteriores podemos somente incrementar o tempo em execucao e decrementar o tempo de duracao atual
                else {
                    sLine += "\nFILA: " + queue.display() + "\nCPU: " + processoEmExecucao.getsName() + "(" + processoEmExecucao.getiCurrentDuration() + ")";
                    iTempoEmExecucao++;
                    processoEmExecucao.setiCurrentDuration();
                    processoEmExecucao.setiRunTime();

                }
            }
        }
        sLine += "\n\n********************************"
                + "\n*TEMPO MEDIO DE ESPERA: " + tempoMedioEspera(iTempoEmEspera, iQtdProcessos) + " ms*"
                + "\n********************************";
        sLine += "\n\n********************************"
                + "\n***** ENCERRANDO SIMULAÇÃO *****"
                + "\n********************************";

        writeFile(sLine);
        System.exit(0);
    }

    public double tempoMedioEspera(double iTempoEspera, int qtdProcessos) {
        return iTempoEspera / qtdProcessos;
    }

}
