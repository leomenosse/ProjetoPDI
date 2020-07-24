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
    Aplica uma equalização do histograma da imagem
     */
    public void equalizarHistograma(){
        int L = maxValue + 1;
        float[] probabilidades = new float[L];
        int[] qtdPixels = new int[L]; //armazena a quantidade de pixels. qtdPixels[10] armazena quantos pixels com valor 10 existe
        int[] pixelsResultantes = new int[L];

        //inicializando vetor
        for (int i = 0; i < L; i++) {
            qtdPixels[i] = 0;
        }

        //contagem dos pixels
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int valorPixel = pixels[i][j];
                qtdPixels[valorPixel]++;
            }
        }

        //cálculo das probabilidades
        for (int i = 0; i < probabilidades.length; i++) {
            probabilidades[i] = ((float) qtdPixels[i]) / (width * height);
        }

        //cálculo do novo valor
        for (int i = 0; i < pixelsResultantes.length; i++) {
            float somaProbabilidades = 0; //soma das probabilidades de 0 até a posição i
            for (int j = 0; j <= i; j++) {
                somaProbabilidades += probabilidades[j];
            }
            pixelsResultantes[i] = (int) (maxValue * somaProbabilidades);
        }

        alterarPixelsAposEqualizacao(pixelsResultantes);
    }

    /*
    Atribui novos valores aos pixels depois de ocorrer a equalização do histograma
     */
    public void alterarPixelsAposEqualizacao(int[] newPixels){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int valorPixel = pixels[i][j];
                pixels[i][j] = newPixels[valorPixel];
            }
        }
    }

    /*
    Aplica um filtro laplaciano passado por parâmetro e retorna a imagem
    filtrada (não somada com a original).
     */
    public int[][] filtroLaplaciano(int[][] filtro){

        int [][] imagemFiltrada = new int[height][width];
        int limHeight = filtro.length / 2;
        int limWidth = filtro[0].length / 2;

        //copiando a imagem original para a filtrada
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                imagemFiltrada[i][j] = pixels[i][j];
            }
        }

        //aplicando nos pixels onde o filtro não ultrapassa a borda
        for (int i = limHeight; i < height - limHeight; i++) {
            for (int j = limWidth; j < width - limWidth; j++) {
                aplicarFiltroNoPixel(i, j, filtro, imagemFiltrada);
            }
        }

        return imagemFiltrada;
    }

    /*
    Atribui novos valores aos pixels depois de ocorrer a filtragem laplaciana
    Antes de chamar esse método, verifique se os valores dos pixels são os valores originais
     */
    public void alterarPixelsAposFiltragemLaplaciana(int[][] resultadoFiltro, int c){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int novoValor = pixels[i][j] + (c * resultadoFiltro[i][j]);

                if(novoValor > maxValue) resultadoFiltro[i][j] = maxValue;
                else if(novoValor < 0) resultadoFiltro[i][j] = 0;
                else resultadoFiltro[i][j] = novoValor;
            }
        }
        setPixels(resultadoFiltro);
    }

    /*
    Aplica o filtro no pixel indicado por parâmetro. As contas são feitas utilizando a matriz
    original pois ela nunca é alterada. O resultado é salvo na matriz imagemFiltrada. Devemos
    passar apenas pixels onde o filtro não extrapole as bordas da imagem
     */
    public void aplicarFiltroNoPixel(int i, int j, int[][] filtro, int[][] imagemFiltrada){
        int heightFiltro = filtro.length;
        int widthFiltro = filtro[0].length;
        int limHeight = heightFiltro / 2;
        int limWidth = widthFiltro / 2;

        int soma = 0;
        int cont = 0;
        int[] arrayFiltro = Util.matrixToArray(filtro);
        if(arrayFiltro == null) return;


        for (int k = i - limHeight; k <= i + limHeight; k++) {
            for (int l = j - limWidth; l <= j + limWidth; l++) {
                soma += pixels[k][l] * arrayFiltro[cont];
                cont++;
            }
        }

        if(soma < 0) imagemFiltrada[i][j] = 0;
        else if(soma > maxValue) imagemFiltrada[i][j] = maxValue;
        else imagemFiltrada[i][j] = soma;
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
