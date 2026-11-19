package br.com.citizentech.app;

import java.util.Scanner;

public class Console {

    private static Scanner scanner = new Scanner(System.in);

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("========== " + texto.toUpperCase() + " ==========");
    }

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem + ": ");
        return scanner.nextLine().trim();
    }

    // Repete a pergunta até a pessoa digitar um número válido
    public static int lerInteiro(String mensagem, int minimo, int maximo) {
        while (true) {
            try {
                int numero = Integer.parseInt(lerTexto(mensagem));
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // cai na mensagem de erro abaixo
            }
            System.out.println("Digite um número de " + minimo + " a " + maximo + ".");
        }
    }

    public static double lerDecimal(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(mensagem).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }
}
