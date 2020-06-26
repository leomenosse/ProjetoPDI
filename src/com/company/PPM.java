package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

        fileType = scanner.next();
        width = scanner.nextInt();
        height = scanner.nextInt();
        maxValue = scanner.nextInt();
        pixels = new int[height][width][3];

        ArrayList<Integer> values = new ArrayList<>();

        while (scanner.hasNext()) {
            if (scanner.hasNextInt())
                values.add(scanner.nextInt());
            else
                scanner.next();
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

    /*
    Salva o arquivo PPM com o nome especificado no parâmetro
    É necessário passar a extensão. Ex: arq.ppm
     */
    public void save(String nomeArq) throws IOException {
        BufferedWriter buffer = new BufferedWriter(new FileWriter((nomeArq)));

        buffer.append("P2" + "\n" + width + " " + height + "\n" + maxValue + "\n");
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
    Salva 3 arquivos PGM, um para cada canal de cor (R, G, B)
    É necessário passar a extensão
    Ex: imagem.ppm -> red_imagem.pgm, green_imagem.pgm e blue_imagem.pgm
     */
    public void saveRGB(String nomeArq) throws IOException {
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

        red_pgm.save("red_"+nomeArq);
        green_pgm.save("green_"+nomeArq);
        blue_pgm.save("blue_"+nomeArq);
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

}
