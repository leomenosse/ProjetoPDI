package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PPM {

    private String fileType;
    private int width;
    private int height;
    private int maxValue; //valor máximo do pixel
    private int[][][] pixels;

    public PPM(String fileType, int width, int height, int maxValue, int[][][] pixels) {
        this.fileType = fileType;
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.pixels = pixels;
    }

    /*
    Esse construtor recebe o nome do arquivo ppm. É feita tanto a
    leitura do cabeçalho quanto a leitura dos valores dos pixels
     */
    public PPM(String nomeArquivo) throws IOException {
        Path filePath = Paths.get(nomeArquivo);
        Scanner scanner = new Scanner(filePath);

        fileType = Util.readFileType(scanner);
        width = Util.readNextInt(scanner);
        height = Util.readNextInt(scanner);
        maxValue = Util.readNextInt(scanner);
        pixels = new int[height][width][3];

        ArrayList<Integer> values = new ArrayList<>();

        while (scanner.hasNext()) {
            values.add(Util.readNextInt(scanner));
        }

        int count = 0;

        for(int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                for(int k = 0; k < 3; k++){
                    pixels[i][j][k] = values.get(count);
                    count++;
                }
            }
        }
    }

    /*
    Esse construtor recebe três objetos pgm, um representando cada canal de cor (RGB)
    A partir desses três, cria-se um ppm
     */
    public PPM(PGM r, PGM g, PGM b){
        fileType = "P3";
        width = r.getWidth();
        height = r.getHeight();
        maxValue = r.getMaxValue();
        pixels = new int[height][width][3];

        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                pixels[i][j][0] = r.getPixels()[i][j];
                pixels[i][j][1] = g.getPixels()[i][j];
                pixels[i][j][2] = b.getPixels()[i][j];
            }
        }
    }

    public PPM(PPM original){
        this.fileType = original.getFileType();
        this.width = original.getWidth();
        this.height = original.getHeight();
        this.maxValue = original.getMaxValue();
        this.pixels = new int[height][width][3];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                for (int k = 0; k < 3; k++) {
                    pixels[i][j][k] = original.getPixel(i, j, k);
                }
            }
        }
    }

    /*
    Salva o arquivo PPM com o nome especificado no parâmetro
    É necessário passar a extensão. Ex: arq.ppm
     */
    public void save(String nomeArq) throws IOException {
        BufferedWriter buffer = new BufferedWriter(new FileWriter((nomeArq)));

        buffer.append(fileType + "\n" + width + " " + height + "\n" + maxValue + "\n");
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                for(int k = 0; k < 3; k++){
                    buffer.append(String.valueOf(pixels[i][j][k])).append(" ");
                }
            }
            buffer.append("\n");
        }

        buffer.close();
    }

    /*
    Retorna uma lista com 3 arquivos PGM, um para cada canal de cor (R, G, B), respectivamente
     */
    public List<PGM> splitRGB() {
        List<PGM> channels = new ArrayList<>();
        int[][] red_channel = new int[height][width];
        int[][] green_channel = new int[height][width];
        int[][] blue_channel = new int[height][width];

        for(int i = 0; i < pixels.length; i++){
            for (int j = 0; j < pixels[i].length; j++){
                red_channel[i][j] = pixels[i][j][0];
                green_channel[i][j] = pixels[i][j][1];
                blue_channel[i][j] = pixels[i][j][2];
            }
        }

        PGM red_pgm = new PGM("P2", width, height, maxValue, red_channel);
        PGM green_pgm = new PGM("P2", width, height, maxValue, green_channel);
        PGM blue_pgm = new PGM("P2", width, height, maxValue, blue_channel);

        channels.add(red_pgm);
        channels.add(green_pgm);
        channels.add(blue_pgm);
        return channels;
    }

    /*
    Salva 3 arquivos PGM, um para cada canal (C, M, Y)
    Não é necessário passar a extensão
    Ex: imagem -> imagem_c.pgm, imagem_m.pgm e imagem_y.pgm
     */
    public void saveCMY(String nomeArq) throws IOException {
        int[][] c = new int[height][width];
        int[][] m = new int[height][width];
        int[][] y = new int[height][width];

        for(int i = 0; i < pixels.length; i++){
            for (int j = 0; j < pixels[i].length; j++){
                c[i][j] = maxValue - pixels[i][j][0];
                m[i][j] = maxValue - pixels[i][j][1];
                y[i][j] = maxValue - pixels[i][j][2];
            }
        }

        PGM c_pgm = new PGM("P2", width, height, maxValue, c);
        PGM m_pgm = new PGM("P2", width, height, maxValue, m);
        PGM y_pgm = new PGM("P2", width, height, maxValue, y);

        c_pgm.save(nomeArq+"_c.pgm");
        m_pgm.save(nomeArq+"_m.pgm");
        y_pgm.save(nomeArq+"_y.pgm");

    }

    /*
    Salva 3 arquivos PGM, um para cada canal (H, S, I)
    Não é necessário passar a extensão
    Ex: imagem -> imagem_h.pgm, imagem_s.pgm e imagem_i.pgm
     */
    public void saveHSI(String nomeArq) throws IOException {
        int[][] h_channel = new int[height][width];
        int[][] s_channel = new int[height][width];
        int[][] i_channel = new int[height][width];

        for(int k = 0; k < pixels.length; k++){
            for (int j = 0; j < pixels[k].length; j++){

                double r = (double) pixels[k][j][0] / (double) (pixels[k][j][0] + pixels[k][j][1] + pixels[k][j][2]);// R/(R+G+B)
                double g = (double) pixels[k][j][1] / (double) (pixels[k][j][0] + pixels[k][j][1] + pixels[k][j][2]);// G/(R+G+B)
                double b = (double) pixels[k][j][2] / (double) (pixels[k][j][0] + pixels[k][j][1] + pixels[k][j][2]);// B/(R+G+B)

                double numerador = 0.5 * ((r-g)+(r-b));
                double denominador = Math.pow(Math.pow(r-g, 2) + (r-b) * (g-b), 0.5);

                if(denominador == 0) h_channel[k][j] = 0;
                else {
                    if(b <= g){
                        h_channel[k][j] = (int) (Math.acos(numerador / denominador) * (maxValue / (2 * Math.PI)));
                    }
                    else{
                        h_channel[k][j] = (int) (((2 * Math.PI) - (Math.acos(numerador / denominador))) * (maxValue / (2 * Math.PI)));
                    }
                }

                s_channel[k][j] = (int) ((1 - 3 * Math.min(Math.min(r, g), b)) * maxValue);
                i_channel[k][j] = (int) (((pixels[k][j][0] + pixels[k][j][1] + pixels[k][j][2]) / (3.0 * maxValue)) * maxValue);
            }
        }

        PGM h_pgm = new PGM("P2", width, height, maxValue, h_channel);
        PGM s_pgm = new PGM("P2", width, height, maxValue, s_channel);
        PGM i_pgm = new PGM("P2", width, height, maxValue, i_channel);

        h_pgm.save(nomeArq+"_h.pgm");
        s_pgm.save(nomeArq+"_s.pgm");
        i_pgm.save(nomeArq+"_i.pgm");

    }

    public void clarear(int intensidade){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                for (int k = 0; k < 3; k++) {
                    int novoValor = pixels[i][j][k] + intensidade;

                    if(novoValor > maxValue) pixels[i][j][k] = maxValue;
                    else pixels[i][j][k] = novoValor;
                }
            }
        }
    }

    public void multiplicar(float intensidade){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                for (int k = 0; k < 3; k++) {
                    int novoValor = (int) (pixels[i][j][k] * intensidade);

                    if(novoValor > maxValue) pixels[i][j][k] = maxValue;
                    else pixels[i][j][k] = novoValor;
                }
            }
        }
    }

    public void subtrair(PPM ppm){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                for (int k = 0; k < 3; k++) {
                    int novoValor = pixels[i][j][k] - ppm.getPixel(i, j, k);

                    if(novoValor < 0) pixels[i][j][k] = 0;
                    else pixels[i][j][k] = novoValor;
                }
            }
        }
    }

    public void somar(PPM ppm){
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                for (int k = 0; k < 3; k++) {
                    int novoValor = pixels[i][j][k] + ppm.getPixel(i, j, k);

                    if(novoValor > maxValue) pixels[i][j][k] = maxValue;
                    else pixels[i][j][k] = novoValor;
                }
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
                for (int k = 0; k < 3; k++) {
                    if(pixels[i][j][k] >= a && pixels[i][j][k] <= b){
                        pixels[i][j][k] = novoValor;
                    }
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
                for (int k = 0; k < 3; k++) {

                    if(pixels[i][j][k] >= a && pixels[i][j][k] <= b){
                        pixels[i][j][k] = valorDentroDoIntervalo;
                    }
                    else{
                        pixels[i][j][k] = valorForaDoIntervalo;
                    }
                }
            }
        }

    }



    public void showInfo(){
        System.out.println("FileType: " + fileType);
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("MaxValue: " + maxValue);

        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                for(int k = 0; k < 3; k++){
                    System.out.printf("%3d ", pixels[i][j][k]);
                }
            }
            System.out.println();
        }
    }

    // GETTERS E SETTERS

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int[][][] getPixels() {
        return pixels;
    }

    public int getPixel(int i, int j, int k){
        return pixels[i][j][k];
    }

    public void setPixels(int[][][] pixels) {
        this.pixels = pixels;
    }

}
