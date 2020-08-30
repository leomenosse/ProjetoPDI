package com.company;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

    /*
    Lê o próximo token do arquivo. Caso ele seja o '#', ignora o resto da linha e vai para a próxima.
    Isso é feito até que um número inteiro seja encontrado (um que não faça parte do comentário)
     */
    public static int readNextInt(Scanner scanner){
        String aux;
        int number = -1;
        if(scanner.hasNext()){
            do{
                aux = scanner.next();
                if(aux.contains("#")){
                    scanner.nextLine();
                    aux = null;
                }
                else{
                    number = Integer.parseInt(aux);
                }
            }while(aux == null);
        }
        return number;
    }

    /*
    Lê o próximo token do arquivo. Caso ele seja o '#', ignora o resto da linha e vai para a próxima.
    É utilizado para ler o tipo do arquivo "P1", "P2"...
     */
    public static String readFileType(Scanner scanner){
        String fileType = null;
        if(scanner.hasNext()){
            do {
                fileType = scanner.next();
                if (fileType.contains("#")) {
                    scanner.nextLine();
                }
            }while(fileType.contains("#"));
        }
        return fileType;
    }

    /*
    Transforma uma matriz de inteiros em um array
     */
    public static int[] matrixToArray(int[][] matrix){
        if(matrix.length == 0) return null;
        if(matrix[0].length == 0) return null;

        int[] array = new int[matrix.length * matrix[0].length];
        int k = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                array[k] = matrix[i][j];
                k++;
            }
        }

        return array;
    }

    public static List lerInstrucoesDoArquivo(String nomeArquivo) throws IOException {
        List<String> instrucoes = new ArrayList<>();
        Path filePath = Paths.get(nomeArquivo);
        Scanner scanner = new Scanner(filePath);

        while(scanner.hasNextLine()){
            instrucoes.add(scanner.nextLine());
        }

        for(String s: instrucoes){
            System.out.println(s);
        }

        return instrucoes;
    }

}
