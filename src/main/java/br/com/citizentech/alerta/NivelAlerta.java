package br.com.citizentech.alerta;

public enum NivelAlerta {

    NORMAL("NORMAL", "dentro do esperado"),
    ATENCAO("ATENÇÃO", "chegando perto do limite"),
    ALERTA("ALERTA", "passou do limite"),
    CRITICO("CRÍTICO", "passou muito do limite");

    private String nome;
    private String significado;

    NivelAlerta(String nome, String significado) {
        this.nome = nome;
        this.significado = significado;
    }

    // ATENÇÃO a partir de 80% do limite, ALERTA a partir de 100% e CRÍTICO a partir de 130%
    public static NivelAlerta calcular(double valor, double limite) {
        double proporcao = valor / limite;
        if (proporcao >= 1.3) {
            return CRITICO;
        }
        if (proporcao >= 1.0) {
            return ALERTA;
        }
        if (proporcao >= 0.8) {
            return ATENCAO;
        }
        return NORMAL;
    }

    public boolean ehMaisGraveQue(NivelAlerta outro) {
        return this.ordinal() > outro.ordinal();
    }

    public String getNome() {
        return nome;
    }

    public String getSignificado() {
        return significado;
    }
}
