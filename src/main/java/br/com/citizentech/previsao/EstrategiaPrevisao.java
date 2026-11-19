package br.com.citizentech.previsao;

import java.util.List;

public abstract class EstrategiaPrevisao {

    private String nome;
    private int minimoDeLeituras;

    public EstrategiaPrevisao(String nome, int minimoDeLeituras) {
        this.nome = nome;
        this.minimoDeLeituras = minimoDeLeituras;
    }

    public abstract double prever(List<Double> leituras);

    public boolean podePrever(List<Double> leituras) {
        return leituras.size() >= minimoDeLeituras;
    }

    public String descrever() {
        return nome + " (precisa de " + minimoDeLeituras + " leituras)";
    }

    protected double calcularMedia(List<Double> valores) {
        double soma = 0;
        for (double valor : valores) {
            soma += valor;
        }
        return soma / valores.size();
    }

    public String getNome() {
        return nome;
    }

    public int getMinimoDeLeituras() {
        return minimoDeLeituras;
    }
}
