package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

//        Menu.show();
        String ppmName = "strawberries.ppm";
        String nomeSemExtensao = ppmName.split("[.]")[0];

        PPM ppm = new PPM(ppmName);
        ppm.saveRGB(nomeSemExtensao);
        ppm.saveCMY(nomeSemExtensao);
        ppm.saveHSI(nomeSemExtensao);
    }
}
