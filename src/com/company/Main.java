package com.company;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String lenaArq = "lena.ascii.pgm";
        String nomeArq = "spine.pgm.txt";

//        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//        } catch (Exception e) {e.printStackTrace();}
//
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
//        int result = fileChooser.showOpenDialog(null);
//        if(result == JFileChooser.APPROVE_OPTION){
//            File selectedFile = fileChooser.getSelectedFile();
//            nomeArq = selectedFile.getName(); //já vem com a extensão
//            System.out.println(nomeArq);
//
//
//        }

        PGM pgmOriginal = new PGM(nomeArq);
        PGM pgm = new PGM(pgmOriginal);

        pgm.transformacaoGama(0.3, 1);
        pgm.save("gama_spine2.pgm");

    }
}
