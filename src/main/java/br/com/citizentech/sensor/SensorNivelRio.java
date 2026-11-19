package br.com.citizentech.sensor;

import br.com.citizentech.componente.ComponenteTerritorial;

public class SensorNivelRio extends ComponenteTerritorial implements Sensor {

    private static final double MINIMO = 0.3;
    private static final double MAXIMO = 8.0;
    private static final double CHANCE_DE_CHEIA = 0.15;
    private static final double CHANCE_DE_FALHA = 0.03;

    private double ultimaLeitura = 1.8;

    public SensorNivelRio(String id, String regiao) {
        super(id, "Régua de nível", regiao);
    }

    // O rio sobe quando tem cheia, senão vai baixando aos poucos
    @Override
    public double lerValor() {
        double variacao = Math.random() * 0.3 - 0.15;
        if (Math.random() < CHANCE_DE_CHEIA) {
            variacao += 0.45;
        } else {
            variacao -= 0.08;
        }
        ultimaLeitura = Math.max(MINIMO, Math.min(MAXIMO, ultimaLeitura + variacao));
        return ultimaLeitura;
    }

    @Override
    public boolean verificarFuncionamento() {
        testarEquipamento(CHANCE_DE_FALHA);
        return estaAtivo();
    }

    @Override
    public String verificarStatus() {
        return String.format("%s %s - %s - última leitura: %.2f %s",
                getId(), getNome(), getStatus(), ultimaLeitura, getUnidade());
    }

    @Override
    public String getTipo() {
        return NIVEL_RIO;
    }

    @Override
    public String getUnidade() {
        return "m";
    }

    @Override
    public double getUltimaLeitura() {
        return ultimaLeitura;
    }
}
