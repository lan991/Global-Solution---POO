package br.com.citizentech.previsao;

import java.util.List;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    private int tamanhoJanela;

    public PrevisaoMediaMovel(int tamanhoJanela) {
        super("Média Móvel", tamanhoJanela);
        this.tamanhoJanela = tamanhoJanela;
    }

    // Média das últimas N leituras
    @Override
    public double prever(List<Double> leituras) {
        int fim = leituras.size();
        List<Double> ultimas = leituras.subList(fim - tamanhoJanela, fim);
        return calcularMedia(ultimas);
    }

    @Override
    public String descrever() {
        return super.descrever() + " - média das últimas " + tamanhoJanela + " leituras";
    }
}
