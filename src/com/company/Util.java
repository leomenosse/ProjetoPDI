package com.company;

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

}
