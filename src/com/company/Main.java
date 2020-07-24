package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

//        Menu.show();

        PGM og = new PGM("blurry_moon.pgm");
        PGM pgm = new PGM(og);

        int[][] filtro = new int[3][3];
        filtro[0][0] = 0;
        filtro[0][1] = -1;
        filtro[0][2] = 0;
        filtro[1][0] = -1;
        filtro[1][1] = 4;
        filtro[1][2] = -1;
        filtro[2][0] = 0;
        filtro[2][1] = -1;
        filtro[2][2] = 0;

        int[] array;
        array = Util.matrixToArray(filtro);
        pgm.filtroLaplaciano(filtro, 1);
        pgm.save("blurry_moon_laplace.pgm");
    }
}
