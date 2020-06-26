package com.company;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String nomeArq = "lena.ascii.pgm";

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

        pgm.girar90Horario();
        pgm.save("90horario_"+nomeArq);

    }
}
