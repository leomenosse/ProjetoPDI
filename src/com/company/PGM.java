package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class PGM{

    private String fileType; //P2
    private int width;
    private int height;
    private int maxValue; //valor máximo do pixel
    private int[][] pixels;

    public PGM(String fileType, int width, int height, int maxValue, int[][] pixels) {
        this.fileType = fileType;
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.pixels = pixels;
    }

    /*
    Esse construtor recebe o nome do arquivo pgm. É feita tanto a
    leitura do cabeçalho quanto a leitura dos valores dos pixels
    É necessário passar o nome do aqruivo junto com sua extensão
     */
    public PGM(String nomeArquivo) throws IOException {
        Path filePath = Paths.get(nomeArquivo);
        Scanner scanner = new Scanner(filePath);

        fileType = Util.readFileType(scanner);
        width = Util.readNextInt(scanner);
        height = Util.readNextInt(scanner);
        maxValue = Util.readNextInt(scanner);
        pixels = new int[height][width];

        ArrayList<Integer> values = new ArrayList<>();

        while (scanner.hasNext()) {
            values.add(Util.readNextInt(scanner));
        }

        int count = 0;

        for(int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = values.get(count);
                count++;
            }
        }
    }

    /*
    Esse construtor recebe um objeto PGM e cria uma cópia (por valor, não por referência) do mesmo
    Fazendo essa deep copy, ao alterarmos um objeto, o outro não será modificado
     */
    public PGM(PGM original){
        this.fileType = original.getFileType();
        this.width = original.getWidth();
        this.height = original.getHeight();
        this.maxValue = original.getMaxValue();
        this.pixels = new int[height][width];

        for(int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = original.getPixel(i, j);
            }
        }
    }

    public void escurecer(int intensidade){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                int novoValor = pixels[i][j] - intensidade;

                if(novoValor < 0) pixels[i][j] = 0;
                else pixels[i][j] = novoValor;
            }
        }
    }

    public void clarear(int intensidade){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                int novoValor = pixels[i][j] + intensidade;

                if(novoValor > maxValue) pixels[i][j] = maxValue;
                else pixels[i][j] = novoValor;
            }
        }
    }

    public void negativo(){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                pixels[i][j] = maxValue - pixels[i][j];
            }
        }
    }

    /*
    Aplica o fatiamento de níveis de intensidade, alterando somente os pixels cuja intensidade está no intervalo [a, b]
     */
    public void fatiamento(int a, int b, int novoValor){

        //verificação que determina se o novo valor está dentro do intervalo permitido
        if(novoValor > this.maxValue) novoValor = maxValue;
        else if(novoValor < 0) novoValor = 0;

        for(int i = 0; i < pixels.length; i++){ //height
            for(int j = 0; j < pixels[i].length; j++){ //width
                if(pixels[i][j] >= a && pixels[i][j] <= b){
                    pixels[i][j] = novoValor;
                }
            }
        }

    }

    /*
    Aplica o fatiamento de níveis de intensidade, alterando todos os pixels.
    Aqueles que estão dentro do intervalo [a,b] recebem valorDentroDoIntervalo
    Os que estão fora do intervalo recebem valorForaDoIntervalo
     */
    public void fatiamento(int a, int b, int valorDentroDoIntervalo, int valorForaDoIntervalo){

        //verificação que determina se o novo valor está dentro do intervalo permitido
        if(valorDentroDoIntervalo > this.maxValue) valorDentroDoIntervalo = maxValue;
        else if(valorDentroDoIntervalo < 0) valorDentroDoIntervalo = 0;

        if(valorForaDoIntervalo > this.maxValue) valorForaDoIntervalo= maxValue;
        else if(valorForaDoIntervalo < 0) valorForaDoIntervalo = 0;

        for(int i = 0; i < pixels.length; i++){ //height
            for(int j = 0; j < pixels[i].length; j++){ //width
                if(pixels[i][j] >= a && pixels[i][j] <= b){
                    pixels[i][j] = valorDentroDoIntervalo;
                }
                else{
                    pixels[i][j] = valorForaDoIntervalo;
                }
            }
        }

    }

    /*
    Aplica a transformação gama cuja fórmula é s = c*r^(gama)
    Onde s é o valor resultante, r é o valor do pixel de entrada, gama e c são constantes
     */
    public void transformacaoGama(double gama, double c){

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                double novoValor = c * Math.pow((float) pixels[i][j] / maxValue, gama);
                pixels[i][j] = (int) (novoValor * maxValue);
            }
        }

    }

    /*
    Gira a imagem em torno do eixo y
     */
    public void flipHorizontal(){

        for (int i = 0; i < pixels.length; i++) {
            //copiando a linha para um vetor auxiliar
            int[] aux = new int[pixels[i].length];
            for (int k = 0; k < aux.length; k++) {
                aux[k] = pixels[i][k];
            }

            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = aux[pixels[i].length - 1 - j];
            }
        }

    }

    public void girar90AntiHorario(){
        int newWidth = height;
        int newHeight = width;
        int[][] newPixels = new int[newHeight][newWidth];

        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                newPixels[width - 1 - j][i] = pixels[i][j];
            }
        }

        height = newHeight;
        width = newWidth;
        pixels = newPixels;

    }

    public void girar90Horario(){
        int newWidth = height;
        int newHeight = width;
        int[][] newPixels = new int[newHeight][newWidth];

        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                newPixels[j][height - 1 - i] = pixels[i][j];
            }
        }

        height = newHeight;
        width = newWidth;
        pixels = newPixels;
    }

    /*
    Salva o arquivo PGM com o nome especificado no parâmetro
    É necessário passar a extensão. Ex: arq.pgm
     */
    public void save(String nomeArq) throws IOException {
        BufferedWriter buffer = new BufferedWriter(new FileWriter((nomeArq)));

        buffer.append(fileType + "\n").append(String.valueOf(width)).append(" ").append(String.valueOf(height)).append("\n").append(String.valueOf(maxValue)).append("\n");
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                buffer.append(String.valueOf(pixels[i][j])).append(" ");
            }
            buffer.append("\n");
        }

        buffer.close();
    }

    public void showInfo(){
        System.out.println("FileType: " + fileType);
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("MaxValue: " + maxValue);

        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                System.out.printf("%3d ", pixels[i][j]);
            }
            System.out.println();
        }
    }

    // GETTERS AND SETTERS

    public String getFileType() {
        return fileType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int[][] getPixels() {
        return pixels;
    }

    public int getPixel(int i, int j){return pixels[i][j];}

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setPixels(int[][] pixels) {
        this.pixels = pixels;
    }

    public void setPixel(int i, int j, int value){
        this.pixels[i][j] = value;
    }
}
