package com.company;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Menu {

    public static void show() throws IOException {
        String[] extensoes = {"PGM", "PPM"};
        String[] modos = {"Único arquivo", "A partir de três arquivos PGM"};
        String[] cores = {"VERMELHO", "VERDE", "AZUL"};
        int escolhaExtensao, escolhaModo;
        String nomeArq;
        String[] nomesArq = null;

        escolhaExtensao = JOptionPane.showOptionDialog(null, "Qual tipo de arquivo deseja abrir?","Abrir arquivo",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,extensoes,extensoes[0]);

        switch (escolhaExtensao){
            case 0:
                nomeArq = selectFile("Selecione o arquivo PGM");
                manipularPGM(nomeArq);
                break;

            case 1:
                escolhaModo = JOptionPane.showOptionDialog(null, "Como deseja abrir o arquivo?","Escolha um modo",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,modos,modos[0]);

                if(escolhaModo == 0){
                    nomesArq = new String[1];
                    nomesArq[0] = selectFile("Selecione o arquivo PPM");
                }
                else if(escolhaModo == 1){
                    nomesArq = new String[3];
                    for (int i = 0; i < nomesArq.length; i++) {
                        JOptionPane.showMessageDialog(null, "Selecione o arquivo PGM relativo ao canal "+cores[i]);
                        nomesArq[i] = selectFile("Canal "+cores[i]);
                    }
                }
                manipularPPM(nomesArq);
                break;

            default:
                System.exit(2);
                break;
        }
    }

    public static String selectFile(String titulo){
        String nomeArq = null;

        //Look and feel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {e.printStackTrace();}

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle(titulo);
        int result = fileChooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            nomeArq = selectedFile.getName();
        }
        else{
            System.exit(2);
        }

        return nomeArq;
    }

    /*
    Recebe como parâmetro o nome do arquivo PGM a ser aberto. Lê-se o arquivo
    e o usuário pode realizar as manipulações desejadas
     */
    public static void manipularPGM(String nomeArq) throws IOException {

        String fileType = getFileType(nomeArq);

        String pgmOptions = "1. Clarear\n" +
                            "2. Escurecer\n" +
                            "3. Negativo\n" +
                            "4. Rotacionar 90° Horário\n" +
                            "5. Rotacionar 90° Anti-horário\n" +
                            "6. Fatiamento\n" +
                            "7. Transformação gama\n" +
                            "8. Flip horizontal\n" +
                            "9. Salvar\n\n" +
                            "Digite uma opção: ";

        int opcao;
        int auxInt;
        int a, b, valorDentroDoIntervalo, valorForaDoIntervalo;
        double gama, c;
        String novoNome;
        boolean continua;
        String[] continuar = {"Sim", "Não"};
        int confirmacao;

        if(fileType.equals("P2")){ //PGM
            PGM pgmOriginal = new PGM(nomeArq);
            PGM pgm = new PGM(pgmOriginal); //mantendo o original sem modificações

            do {

                opcao = Integer.parseInt(JOptionPane.showInputDialog(pgmOptions));

                switch (opcao) {
                    case 1:
                        auxInt = Integer.parseInt(JOptionPane.showInputDialog("Digite uma intensidade para clarear"));
                        pgm.clarear(auxInt);
                        break;

                    case 2:
                        auxInt = Integer.parseInt(JOptionPane.showInputDialog("Digite uma intensidade para escurecer"));
                        pgm.escurecer(auxInt);
                        break;

                    case 3:
                        pgm.negativo();
                        break;

                    case 4:
                        pgm.girar90Horario();
                        break;

                    case 5:
                        pgm.girar90AntiHorario();
                        break;

                    case 6:
                        auxInt = Integer.parseInt(JOptionPane.showInputDialog("1. Alterar somente valores entre um intervalo\n" +
                                "2. Alterar todos os valores\n\n" +
                                "Digite uma opção"));
                        if (auxInt == 1) {
                            a = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para a"));
                            b = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para b"));
                            valorDentroDoIntervalo = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para os pixels no intervalo [a, b]"));
                            pgm.fatiamento(a, b, valorDentroDoIntervalo);
                        } else if (auxInt == 2) {
                            a = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para a"));
                            b = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para b"));
                            valorDentroDoIntervalo = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para os pixels no intervalo [a, b]"));
                            valorForaDoIntervalo = Integer.parseInt(JOptionPane.showInputDialog("Digite um valor para os pixels fora do intervalo [a, b]"));
                            pgm.fatiamento(a, b, valorDentroDoIntervalo, valorForaDoIntervalo);
                        }
                        break;

                    case 7:
                        gama = Double.parseDouble(JOptionPane.showInputDialog("Digite um valor para gama"));
                        c = Double.parseDouble(JOptionPane.showInputDialog("Digite um valor para c"));
                        pgm.transformacaoGama(gama, c);
                        break;

                    case 8:
                        pgm.flipHorizontal();
                        break;

                    case 9:
                        novoNome = JOptionPane.showInputDialog(null, "Digite o nome (com extensão) do arquivo a ser salvo",
                                "Salvar", JOptionPane.INFORMATION_MESSAGE);
                        pgm.save(novoNome);
                        break;

                    default:
                        break;
                }

                confirmacao = JOptionPane.showOptionDialog(null, "Deseja fazer mais alguma operação?", "Confirmação", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, continuar, continuar[0]);
                continua = confirmacao == 0;

            }while(continua);
        }


    }

    /*
    Recebe como parâmetro um vetor de Strings. Se seu tamanho for um, significa que o usuário abriu um arquivo PPM.
    Se o tamanho for três, o usuário escolheu três arquivos PGM para formar um PPM.
    Nesse último caso, ele deve selecionar os arquivos dos canais vermelho, verde e azul (deve ser nesta ordem)
     */
    public static void manipularPPM(String[] nomesArq) throws IOException {

        PPM ppmOriginal = null;
        PPM ppm = null;

        if(nomesArq.length == 1){
            ppmOriginal = new PPM(nomesArq[0]);
        }
        else if(nomesArq.length == 3){
            for(String nomeArq: nomesArq){
                if(!getFileType(nomeArq).equals("P2")){ //Se forem três arquivos mas um deles não for pgm
                    JOptionPane.showMessageDialog(null,"Você deve selecionar três arquivos pgm","Erro",JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }

            PGM r = new PGM(nomesArq[0]);
            PGM g = new PGM(nomesArq[1]);
            PGM b = new PGM(nomesArq[2]);
            ppmOriginal = new PPM(r, g, b);
        }
        else{ //número inválido de argumentos
            JOptionPane.showMessageDialog(null,"Você deve selecionar três arquivos pgm","Erro",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        ppm = new PPM(ppmOriginal);

        String ppmOptions = "1. Salvar\n" +
                            "2. Salvar canais de cor separadamente\n\n" +
                            "Digite uma opção: ";

        int opcao;
        String novoNome;
        int confirmacao;
        String[] continuar = {"Sim", "Não"};
        boolean continua;

        do{
            opcao = Integer.parseInt(JOptionPane.showInputDialog(ppmOptions));

            switch (opcao){
                case 1:
                    novoNome = JOptionPane.showInputDialog(null, "Digite o nome (com extensão) do arquivo a ser salvo",
                            "Salvar", JOptionPane.INFORMATION_MESSAGE);
                    ppm.save(novoNome);
                    break;

                case 2:
                    novoNome = JOptionPane.showInputDialog(null, "Digite um nome (com extensão) para salvar os arquivos.\n" +
                                                            "Exemplo: \"teste.txt\" resultará em red_teste.txt, green_teste.txt e blue_text.txt\n\n",
                            "Salvar canais de cor separadamente", JOptionPane.INFORMATION_MESSAGE);
                    ppm.saveRGB(novoNome);
                    break;

                default:
                    break;
            }

            confirmacao = JOptionPane.showOptionDialog(null, "Deseja fazer mais alguma operação?", "Confirmação", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, continuar, continuar[0]);
            continua = confirmacao == 0;

        }while(continua);

    }

    /*
    Retorna a extensão de um arquivo (contém o '.' no começo)
     */
    public static String getExtension(String nomeArq){

        int index = nomeArq.lastIndexOf('.');
//        String nomeSemExtensao = nomeArq.substring(0, index);

        return nomeArq.substring(index);

    }

    /*
    Retorna a primeira linha do arquivo
    É utilizado para verificar seu tipo(P1, P2, ...)
     */
    public static String getFileType(String nomeArq) throws IOException {

        Path filePath = Paths.get(nomeArq);
        Scanner scanner = new Scanner(filePath);

        return scanner.next();

    }

}
