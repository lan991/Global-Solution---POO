package br.com.citizentech.previsao;

import java.util.List;

public class PrevisaoComparacao extends EstrategiaPrevisao {

    private int tamanhoPeriodo;

    public PrevisaoComparacao(int tamanhoPeriodo) {
        super("Comparação de Períodos", tamanhoPeriodo * 2);
        this.tamanhoPeriodo = tamanhoPeriodo;
    }

    // Compara o período atual com o anterior e repete a variação
    @Override
    public double prever(List<Double> leituras) {
        int fim = leituras.size();
        double mediaAtual = calcularMedia(leituras.subList(fim - tamanhoPeriodo, fim));
        double mediaAnterior = calcularMedia(leituras.subList(fim - tamanhoPeriodo * 2, fim - tamanhoPeriodo));

        double variacao = mediaAtual - mediaAnterior;
        return Math.max(0, mediaAtual + variacao);
    }

    @Override
    public String descrever() {
        return super.descrever() + " - compara as últimas " + tamanhoPeriodo + " leituras com as " + tamanhoPeriodo + " anteriores";
    }
}
