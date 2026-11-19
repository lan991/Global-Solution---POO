package br.com.citizentech.territorio;

import br.com.citizentech.sensor.Sensor;

public class DadosTerritorio {

    private String nomeRegiao;
    private int populacao;
    private String codigoAcesso;
    private double limiteChuva = 30.0;
    private double limiteNivelRio = 3.5;
    private double limiteUmidade = 85.0;

    public DadosTerritorio(String nomeRegiao, int populacao, String codigoAcesso) {
        setNomeRegiao(nomeRegiao);
        setPopulacao(populacao);
        this.codigoAcesso = codigoAcesso;
    }

    public String getNomeRegiao() {
        return nomeRegiao;
    }

    public void setNomeRegiao(String nomeRegiao) {
        if (nomeRegiao == null || nomeRegiao.trim().length() < 3) {
            throw new IllegalArgumentException("O nome da região precisa ter pelo menos 3 letras.");
        }
        this.nomeRegiao = nomeRegiao.trim();
    }

    public int getPopulacao() {
        return populacao;
    }

    public void setPopulacao(int populacao) {
        if (populacao < 0) {
            throw new IllegalArgumentException("A população não pode ser negativa.");
        }
        this.populacao = populacao;
    }

    public double getLimite(String tipoSensor) {
        switch (tipoSensor) {
            case Sensor.CHUVA:
                return limiteChuva;
            case Sensor.NIVEL_RIO:
                return limiteNivelRio;
            default:
                return limiteUmidade;
        }
    }

    // Só altera o limite se o código estiver certo e o valor dentro da faixa
    public void alterarLimite(String tipoSensor, double novoLimite, String codigo) {
        validarCodigo(codigo);
        double maximo = getLimiteMaximo(tipoSensor);
        if (novoLimite <= 0 || novoLimite > maximo) {
            throw new IllegalArgumentException("O limite de " + tipoSensor + " deve ser maior que 0 e no máximo " + maximo + ".");
        }

        switch (tipoSensor) {
            case Sensor.CHUVA:
                limiteChuva = novoLimite;
                break;
            case Sensor.NIVEL_RIO:
                limiteNivelRio = novoLimite;
                break;
            default:
                limiteUmidade = novoLimite;
        }
    }

    public void alterarCodigoAcesso(String codigoAtual, String novoCodigo) {
        validarCodigo(codigoAtual);
        if (novoCodigo == null || !novoCodigo.matches("\\d{4}")) {
            throw new IllegalArgumentException("O novo código precisa ter 4 números.");
        }
        codigoAcesso = novoCodigo;
    }

    private void validarCodigo(String codigo) {
        if (!codigoAcesso.equals(codigo)) {
            throw new IllegalArgumentException("Código de acesso incorreto.");
        }
    }

    private double getLimiteMaximo(String tipoSensor) {
        switch (tipoSensor) {
            case Sensor.CHUVA:
                return 150.0;
            case Sensor.NIVEL_RIO:
                return 10.0;
            default:
                return 100.0;
        }
    }
}
