package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

//        Menu.show();

        PGM blurryMoonOriginal = new PGM("blurry_moon.pgm");
        PGM blurringOriginal = new PGM("blurring.pgm");
        PGM hubbleOriginal = new PGM("hubble.pgm");

        int[][] filtro1 = new int[3][3];
        filtro1[0][0] = 0;
        filtro1[0][1] = -1;
        filtro1[0][2] = 0;
        filtro1[1][0] = -1;
        filtro1[1][1] = 4;
        filtro1[1][2] = -1;
        filtro1[2][0] = 0;
        filtro1[2][1] = -1;
        filtro1[2][2] = 0;

        int[][] filtro2 = new int[3][3];
        filtro2[0][0] = -1;
        filtro2[0][1] = -1;
        filtro2[0][2] = -1;
        filtro2[1][0] = -1;
        filtro2[1][1] = 8;
        filtro2[1][2] = -1;
        filtro2[2][0] = -1;
        filtro2[2][1] = -1;
        filtro2[2][2] = -1;

        /* FILTRO LAPLACIANO COM blurry_moon.pgm */
        PGM blurryMoonFiltro = new PGM(blurryMoonOriginal);

        int[][] imagemFiltrada = blurryMoonFiltro.filtroLaplaciano(filtro1);
        blurryMoonFiltro.setPixels(imagemFiltrada);
        blurryMoonFiltro.save("blurry_moon_filtro1.pgm");

        blurryMoonFiltro.setPixels(blurryMoonOriginal.getPixels());
        blurryMoonFiltro.alterarPixelsAposFiltragemLaplaciana(imagemFiltrada, 1);
        blurryMoonFiltro.save("blurry_moon_resultado1.pgm");

        blurryMoonFiltro = new PGM(blurryMoonOriginal);

        imagemFiltrada = blurryMoonFiltro.filtroLaplaciano(filtro2);
        blurryMoonFiltro.setPixels(imagemFiltrada);
        blurryMoonFiltro.save("blurry_moon_filtro2.pgm");

        blurryMoonFiltro.setPixels(blurryMoonOriginal.getPixels());
        blurryMoonFiltro.alterarPixelsAposFiltragemLaplaciana(imagemFiltrada, 1);
        blurryMoonFiltro.save("blurry_moon_resultado2.pgm");

        /* Filtro da média com blurring.pgm E hubble.pgm */

        int[] sizes = {3, 5, 9, 15, 35};

        for(int s: sizes){
            PGM blurringFiltro = new PGM(blurringOriginal);
            imagemFiltrada = blurringFiltro.filtroMedia(s, s);
            blurringFiltro.alterarPixelsAposFiltragemMedia(imagemFiltrada);
            blurringFiltro.save(String.format("blurring_filtro%dx%d.pgm", s, s));

            PGM hubbleFiltro = new PGM(hubbleOriginal);
            imagemFiltrada = hubbleFiltro.filtroMedia(s, s);
            hubbleFiltro.alterarPixelsAposFiltragemMedia(imagemFiltrada);
            hubbleFiltro.save(String.format("hubble_filtro%dx%d.pgm", s, s));
            if(s == 15){ //binarização
                hubbleFiltro.fatiamento(0, 127, 0, 255);
                hubbleFiltro.save("hubble_filtro15x15_binarizado.pgm");
            }
        }
    }
}
