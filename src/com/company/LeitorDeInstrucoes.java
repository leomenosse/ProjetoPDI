package com.company;

import java.io.IOException;
import java.util.List;

public class LeitorDeInstrucoes {

    public static void executar(List<String> instrucoes) throws IOException {

        PGM pgmOriginal = null, pgmCopia = null;
        PPM ppmOriginal = null, ppmCopia = null;
        PGM r = null, g = null, b = null;

        for(String instrucao: instrucoes){
            String[] tokensInstrucao = instrucao.split(" ");

            switch(tokensInstrucao[0]){
                case "ler":
                    if(tokensInstrucao.length == 2){ //instrução e um parâmetro
                        String nomeArquivo = tokensInstrucao[1];
                        String extensao = Util.getExtension(nomeArquivo);

                        if(extensao.equals("pgm")){
                            pgmOriginal = new PGM(nomeArquivo); //ler pgm
                            pgmCopia = new PGM(pgmOriginal);
                            System.out.println("Leitura de pgm: "+ nomeArquivo);
                        }
                        else if(extensao.equals("ppm")){
                            ppmOriginal = new PPM(nomeArquivo); //ler ppm
                            ppmCopia = new PPM(ppmOriginal);
                            List<PGM> canais = ppmCopia.splitRGB();
                            r = canais.get(0);
                            g = canais.get(1);
                            b = canais.get(2);
                            System.out.println("Leitura de ppm: " + nomeArquivo);
                        }
                    }
                    else if(tokensInstrucao.length == 4){ //instrução e 3 arquivos pgm pra formar 1 ppm
                        r = new PGM(tokensInstrucao[1]);
                        g = new PGM(tokensInstrucao[2]);
                        b = new PGM(tokensInstrucao[3]);
                        ppmOriginal = new PPM(r, g, b);
                        ppmCopia = new PPM(ppmOriginal);
                        System.out.println("Leitura de ppm a partir dos canais RGB: " + tokensInstrucao[1] + " e " +
                                tokensInstrucao[2] + " e " + tokensInstrucao[3]);
                    }
                    break;

                case "escrever":
                    if(tokensInstrucao.length == 2){ //instrução e um parametro
                        String nomeArquivo = tokensInstrucao[1];
                        String extensao = Util.getExtension(nomeArquivo);

                        if(extensao.equals("pgm") && pgmCopia != null) {
                            pgmCopia.save(nomeArquivo); //grava um pgm
                            System.out.println("Escrita de pgm: " + nomeArquivo);
                        }
                        else if(extensao.equals("ppm") && ppmCopia != null) {
//                            ppmCopia.save(nomeArquivo); //grava um ppm
                            ppmCopia = new PPM(r, g, b);
                            ppmCopia.save(nomeArquivo);
                            System.out.println("Escrita de ppm: " + nomeArquivo);
                        }
                    }
                    else if(tokensInstrucao.length == 3){
                        String nomeArquivo = tokensInstrucao[1];
                        String extensao = Util.getExtension(nomeArquivo);
                        String canal = tokensInstrucao[2];

                        if(extensao.equals("pgm") && ppmCopia != null){
                            switch (canal) {
                                case "r":
                                    r.save(nomeArquivo);
                                    break;
                                case "g":
                                    g.save(nomeArquivo);
                                    break;
                                case "b":
                                    b.save(nomeArquivo);
                                    break;
                            }
                            System.out.println("Escrita de pgm: " + nomeArquivo + " referente ao canal " + canal);
                        }
                    }
                    else if(tokensInstrucao.length == 4 && ppmCopia != null){ //instrução e mais 3 parametros
//                        ppmCopia.saveRGB(tokensInstrucao[1], tokensInstrucao[2], tokensInstrucao[3]);
                        r.save(tokensInstrucao[1]);
                        g.save(tokensInstrucao[2]);
                        b.save(tokensInstrucao[3]);
                        System.out.println("Escrita canais RGB a partir de um ppm: " + tokensInstrucao[1] + " e " +
                                tokensInstrucao[2] + " e " + tokensInstrucao[3]);
                    }
                    break;

                case "somar":
                    if(tokensInstrucao.length == 2){
                        try{ //soma com um valor
                            int intensidade = Integer.parseInt(tokensInstrucao[1]);
                            if(pgmCopia != null) pgmCopia.clarear(intensidade);
                            else if(ppmCopia != null) {
                                r.clarear(intensidade);
                                g.clarear(intensidade);
                                b.clarear(intensidade);
                            }
                            System.out.println("Soma com constante " + intensidade);
                        }catch(Exception e){ //soma com outra imagem
                            String nomeArquivo = tokensInstrucao[1];
                            String extensao = Util.getExtension(nomeArquivo);

                            if(extensao.equals("pgm") && pgmCopia != null){
                                PGM novoPgm = new PGM(nomeArquivo);
                                pgmCopia.somar(novoPgm);
                                System.out.println("Soma com arquivo pgm " + nomeArquivo);
                            }
                            else if(extensao.equals("ppm") && ppmCopia != null){
                                PPM novoPpm = new PPM(nomeArquivo);
                                List<PGM> canais = novoPpm.splitRGB();
                                r.somar(canais.get(0));
                                g.somar(canais.get(1));
                                b.somar(canais.get(2));
//                                ppmCopia.somar(novoPpm);
                                System.out.println("Soma com arquivo ppm " + nomeArquivo);
                            }
                        }
                    }
                    else if (tokensInstrucao.length == 3){
                        try{ //somar um valor com um canal da imagem ppm
                            int intensidade = Integer.parseInt(tokensInstrucao[1]);
                            String canal = tokensInstrucao[2];
                            if(ppmCopia != null){ //canais rgb já foram inicializados e não são null
                                switch (canal) {
                                    case "r":
                                        r.clarear(intensidade);
                                        break;
                                    case "g":
                                        g.clarear(intensidade);
                                        break;
                                    case "b":
                                        b.clarear(intensidade);
                                        break;
                                }
                                System.out.println("Soma com constante " + intensidade + " no canal " + canal);
                            }
                        } catch(Exception e){ //somar um arquivo com um canal da imagem ppm
                            String nomeArquivo = tokensInstrucao[1];
                            String extensao = Util.getExtension(nomeArquivo);
                            String canal = tokensInstrucao[2];

                            if(extensao.equals("pgm") && ppmCopia != null){
                                PGM novoPgm = new PGM(nomeArquivo);
                                switch (canal) {
                                    case "r":
                                        r.somar(novoPgm);
                                        break;
                                    case "g":
                                        g.somar(novoPgm);
                                        break;
                                    case "b":
                                        b.somar(novoPgm);
                                        break;
                                }
                                System.out.println("Soma com arquivo pgm" + nomeArquivo + " no canal " + canal);
                            }
                        }
                    }
                    break;

                case "multiplicar":
                    if(tokensInstrucao.length == 2){
                        try{
                            float intensidade = Float.parseFloat(tokensInstrucao[1]);
                            if(pgmCopia != null) pgmCopia.multiplicar(intensidade);
                            else if(ppmCopia != null){
                                r.multiplicar(intensidade);
                                g.multiplicar(intensidade);
                                b.multiplicar(intensidade);
//                                ppmCopia.multiplicar(intensidade);
                            }
                            System.out.println("Multiplicacao por: " + intensidade);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(tokensInstrucao.length == 3){ //multiplicar um canal de cor
                        float intensidade = Float.parseFloat(tokensInstrucao[1]);
                        String canal = tokensInstrucao[2];

                        if(ppmCopia != null){ //canais rgb já foram inicializados e não são null
                            switch (canal) {
                                case "r":
                                    r.multiplicar(intensidade);
                                    break;
                                case "g":
                                    g.multiplicar(intensidade);
                                    break;
                                case "b":
                                    b.multiplicar(intensidade);
                                    break;
                            }
                            System.out.println("Multiplicacao por constante " + intensidade + " no canal " + canal);
                        }
                    }
                    break;

                case "subtrair":
                    if(tokensInstrucao.length == 2){
                        String nomeArquivo = tokensInstrucao[1];
                        String extensao = Util.getExtension(nomeArquivo);

                        if(extensao.equals("pgm") && pgmCopia != null){
                            PGM novoPgm = new PGM(nomeArquivo);
                            pgmCopia.subtrair(novoPgm);
                            System.out.println("Subtração com arquivo pgm " + nomeArquivo);
                        }
                        else if(extensao.equals("ppm") && ppmCopia != null){
                            PPM novoPpm = new PPM(nomeArquivo);
                            List<PGM> canais = novoPpm.splitRGB();
                            r.subtrair(canais.get(0));
                            g.subtrair(canais.get(1));
                            b.subtrair(canais.get(2));
//                            ppmCopia.subtrair(novoPpm);
                            System.out.println("Subtração com arquivo ppm " + nomeArquivo);
                        }
                    }
                    else if(tokensInstrucao.length == 3){ //subtração em apenas um canal de cor
                        String nomeArquivo = tokensInstrucao[1];
                        String extensao = Util.getExtension(nomeArquivo);
                        String canal = tokensInstrucao[2];

                        if(extensao.equals("pgm") && ppmCopia != null){
                            PGM novoPgm = new PGM(nomeArquivo);
                            switch (canal) {
                                case "r":
                                    r.subtrair(novoPgm);
                                    break;
                                case "g":
                                    g.subtrair(novoPgm);
                                    break;
                                case "b":
                                    b.subtrair(novoPgm);
                                    break;
                            }
                            System.out.println("Subtracao com arquivo " + nomeArquivo + " no canal " + canal);
                        }
                    }
                    break;

                case "fatiar":
                    if(tokensInstrucao.length == 4){
                        try{
                            int ini = Integer.parseInt(tokensInstrucao[1]);
                            int fim = Integer.parseInt(tokensInstrucao[2]);
                            int valorDentroDoIntervalo = Integer.parseInt(tokensInstrucao[3]);
                            if(pgmCopia != null){
                                pgmCopia.fatiamento(ini, fim, valorDentroDoIntervalo);
                                System.out.println("Fatiamento de pgm: [" + ini + ", " + fim + "] Dentro do intervalo: " + valorDentroDoIntervalo);
                            }
                            else if(ppmCopia != null){
                                r.fatiamento(ini, fim, valorDentroDoIntervalo);
                                g.fatiamento(ini, fim, valorDentroDoIntervalo);
                                b.fatiamento(ini, fim, valorDentroDoIntervalo);
//                                ppmCopia.fatiamento(ini, fim, novoValor);
                                System.out.println("Fatiamento de ppm: [" + ini + ", " + fim + "] Dentro do intervalo: " + valorDentroDoIntervalo);
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(tokensInstrucao.length == 5){
                        try{
                            int ini = Integer.parseInt(tokensInstrucao[1]);
                            int fim = Integer.parseInt(tokensInstrucao[2]);
                            int valorDentroDoIntervalo = Integer.parseInt(tokensInstrucao[3]);
                            int valorForaDoIntervalo = Integer.parseInt(tokensInstrucao[4]);
                            if(pgmCopia != null){
                                pgmCopia.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                System.out.println("Fatiamento de pgm: [" + ini + ", " + fim + "] Dentro do intervalo: "
                                        + valorDentroDoIntervalo + " Fora do intervalo: " + valorForaDoIntervalo);
                            }
                            else if(ppmCopia != null){
                                r.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                g.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                b.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
//                                ppmCopia.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                System.out.println("Fatiamento de ppm: [" + ini + ", " + fim + "] Dentro do intervalo: "
                                        + valorDentroDoIntervalo + " Fora do intervalo: " + valorForaDoIntervalo);
                            }
                        } catch(Exception e){ //fatiamento com valor dentro do intervalo em só um canal
                            try{
                                int ini = Integer.parseInt(tokensInstrucao[1]);
                                int fim = Integer.parseInt(tokensInstrucao[2]);
                                int valorDentroDoIntervalo = Integer.parseInt(tokensInstrucao[3]);
                                String canal = tokensInstrucao[4];

                                if(ppmCopia != null){
                                    switch (canal) {
                                        case "r":
                                            r.fatiamento(ini, fim, valorDentroDoIntervalo);
                                            break;
                                        case "g":
                                            g.fatiamento(ini, fim, valorDentroDoIntervalo);
                                            break;
                                        case "b":
                                            b.fatiamento(ini, fim, valorDentroDoIntervalo);
                                            break;
                                    }
                                    System.out.println("Fatiamento no canal " + canal + ": [" + ini + ", " + fim + "] Dentro do intervalo: " + valorDentroDoIntervalo);
                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                    else if(tokensInstrucao.length == 6){ //fatiamento com valor dentro e fora do intervalo em um canal
                        try{
                            int ini = Integer.parseInt(tokensInstrucao[1]);
                            int fim = Integer.parseInt(tokensInstrucao[2]);
                            int valorDentroDoIntervalo = Integer.parseInt(tokensInstrucao[3]);
                            int valorForaDoIntervalo = Integer.parseInt(tokensInstrucao[4]);
                            String canal = tokensInstrucao[5];

                            if(ppmCopia != null){
                                switch (canal) {
                                    case "r":
                                        r.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                        break;
                                    case "g":
                                        g.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                        break;
                                    case "b":
                                        b.fatiamento(ini, fim, valorDentroDoIntervalo, valorForaDoIntervalo);
                                        break;
                                }
                                System.out.println("Fatiamento no canal " + canal + ": [" + ini + ", " + fim + "] Dentro do intervalo: "
                                        + valorDentroDoIntervalo + " Fora do intervalo: " + valorForaDoIntervalo);
                            }
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    break;

                case "filtrar-media":
                    if(tokensInstrucao.length == 2){ //filtro da média em todos canais
                        try{
                            int dimensao = Integer.parseInt(tokensInstrucao[1]);

                            if(pgmCopia != null) {
                                pgmCopia.filtroMedia(dimensao, dimensao);
                            }
                            else if(ppmCopia != null){
                                r.filtroMedia(dimensao, dimensao);
                                g.filtroMedia(dimensao, dimensao);
                                b.filtroMedia(dimensao, dimensao);
                            }
                            System.out.println("Filtro da media com dimensao " + dimensao + "x" + dimensao);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(tokensInstrucao.length == 3){ //filtro da média em apenas um canal de cor
                        try{
                            int dimensao = Integer.parseInt(tokensInstrucao[1]);
                            String canal = tokensInstrucao[2];

                            if(ppmCopia != null){
                                switch (canal) {
                                    case "r":
                                        r.filtroMedia(dimensao, dimensao);
                                        break;
                                    case "g":
                                        g.filtroMedia(dimensao, dimensao);
                                        break;
                                    case "b":
                                        b.filtroMedia(dimensao, dimensao);
                                        break;
                                }
                            }
                            System.out.println("Filtro da media com dimensao " + dimensao + "x" + dimensao + " no canal " + canal);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;

                case "filtrar-mediana":
                    if(tokensInstrucao.length == 2){ //filtro da mediana em todos canais
                        try{
                            int dimensao = Integer.parseInt(tokensInstrucao[1]);

                            if(pgmCopia != null) {
                                pgmCopia.filtroMediana(dimensao, dimensao);
                            }
                            else if(ppmCopia != null){
                                r.filtroMediana(dimensao, dimensao);
                                g.filtroMediana(dimensao, dimensao);
                                b.filtroMediana(dimensao, dimensao);
                            }
                            System.out.println("Filtro da mediana com dimensao " + dimensao + "x" + dimensao);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(tokensInstrucao.length == 3){
                        try{
                            int dimensao = Integer.parseInt(tokensInstrucao[1]);
                            String canal = tokensInstrucao[2];

                            if(ppmCopia != null){
                                switch (canal) {
                                    case "r":
                                        r.filtroMediana(dimensao, dimensao);
                                        break;
                                    case "g":
                                        g.filtroMediana(dimensao, dimensao);
                                        break;
                                    case "b":
                                        b.filtroMediana(dimensao, dimensao);
                                        break;
                                }
                            }
                            System.out.println("Filtro da mediana com dimensao " + dimensao + "x" + dimensao + " no canal " + canal);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;

                case "filtrar-laplaciano":
                    int[][] filtroQuatro = new int[3][3];

                    //definição manual dos filtros
                    //TODO funcao pra gerar automaticamente
                    filtroQuatro[0][0] = 0;
                    filtroQuatro[0][1] = -1;
                    filtroQuatro[0][2] = 0;
                    filtroQuatro[1][0] = -1;
                    filtroQuatro[1][1] = 4;
                    filtroQuatro[1][2] = -1;
                    filtroQuatro[2][0] = 0;
                    filtroQuatro[2][1] = -1;
                    filtroQuatro[2][2] = 0;

                    int[][] filtroOito = new int[3][3];
                    filtroOito[0][0] = -1;
                    filtroOito[0][1] = -1;
                    filtroOito[0][2] = -1;
                    filtroOito[1][0] = -1;
                    filtroOito[1][1] = 8;
                    filtroOito[1][2] = -1;
                    filtroOito[2][0] = -1;
                    filtroOito[2][1] = -1;
                    filtroOito[2][2] = -1;

                    int[][] filtroQuatroNeg = new int[3][3];
                    filtroQuatroNeg[0][0] = 0;
                    filtroQuatroNeg[0][1] = 1;
                    filtroQuatroNeg[0][2] = 0;
                    filtroQuatroNeg[1][0] = 1;
                    filtroQuatroNeg[1][1] = -4;
                    filtroQuatroNeg[1][2] = 1;
                    filtroQuatroNeg[2][0] = 0;
                    filtroQuatroNeg[2][1] = 1;
                    filtroQuatroNeg[2][2] = 0;

                    int[][] filtroOitoNeg = new int[3][3];
                    filtroOitoNeg[0][0] = 1;
                    filtroOitoNeg[0][1] = 1;
                    filtroOitoNeg[0][2] = 1;
                    filtroOitoNeg[1][0] = 1;
                    filtroOitoNeg[1][1] = -8;
                    filtroOitoNeg[1][2] = 1;
                    filtroOitoNeg[2][0] = 1;
                    filtroOitoNeg[2][1] = 1;
                    filtroOitoNeg[2][2] = 1;

                    try{

                        int elementoCentral = Integer.parseInt(tokensInstrucao[1]);
                        int cte = 0;
                        int[][] filtro = null;
                        if(elementoCentral == 4){
                            filtro = filtroQuatro;
                            cte = 1;
                        }
                        else if(elementoCentral == -4){
                            filtro = filtroQuatroNeg;
                            cte = -1;
                        }
                        else if(elementoCentral == 8){
                            filtro = filtroOito;
                            cte = 1;
                        }
                        else if(elementoCentral == -8){
                            filtro = filtroOitoNeg;
                            cte = -1;
                        }


                        if(tokensInstrucao.length == 2){ //filtro laplaciano em um pgm
                            if(pgmCopia != null){
                                pgmCopia.alterarPixelsAposFiltragemLaplaciana(pgmCopia.filtroLaplaciano(filtro), cte);
                                System.out.println("Filtro laplaciano com valor central " + elementoCentral + " em pgm");
                            }
                            else if(ppmCopia != null){ //filtro laplaciano em todos os canais
                                r.alterarPixelsAposFiltragemLaplaciana(r.filtroLaplaciano(filtro), cte);
                                g.alterarPixelsAposFiltragemLaplaciana(g.filtroLaplaciano(filtro), cte);
                                b.alterarPixelsAposFiltragemLaplaciana(b.filtroLaplaciano(filtro), cte);
                                System.out.println("Filtro laplaciano com valor central " + elementoCentral + " em ppm");
                            }
                        }
                        else if(tokensInstrucao.length == 3){ //filtro laplaciano só em um canal
                            String canal = tokensInstrucao[2];

                            if(ppmCopia != null){
                                switch (canal) {
                                    case "r":
                                        r.alterarPixelsAposFiltragemLaplaciana(r.filtroLaplaciano(filtro), cte);
                                        break;
                                    case "g":
                                        g.alterarPixelsAposFiltragemLaplaciana(g.filtroLaplaciano(filtro), cte);
                                        break;
                                    case "b":
                                        b.alterarPixelsAposFiltragemLaplaciana(b.filtroLaplaciano(filtro), cte);
                                        break;
                                }
                                System.out.println("Filtro laplaciano com valor central " + elementoCentral + " no canal " + canal);
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;

                case "equalizar":
                    if(tokensInstrucao.length == 1){

                        if(pgmCopia != null){
                            pgmCopia.equalizarHistograma();
                            System.out.println("Equalizacao de histograma no pgm");
                        }
                        else if(ppmCopia != null){
                            r.equalizarHistograma();
                            g.equalizarHistograma();
                            b.equalizarHistograma();
                            System.out.println("Equalizacao de histograma no ppm");
                        }
                    }
                    else if(tokensInstrucao.length == 2){
                        String canal = tokensInstrucao[1];
                        if(ppmCopia != null){
                            switch (canal) {
                                case "r":
                                    r.equalizarHistograma();
                                    break;
                                case "g":
                                    g.equalizarHistograma();
                                    break;
                                case "b":
                                    b.equalizarHistograma();
                                    break;
                            }
                            System.out.println("Equalizacao de histograma no canal " + canal);
                        }
                    }
                    break;

                default: break;
            }

        }

    }

}
