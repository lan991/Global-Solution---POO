package br.com.citizentech.sensor;

import br.com.citizentech.componente.ComponenteTerritorial;

public class SensorUmidade extends ComponenteTerritorial implements Sensor {

    private static final double MINIMO = 20.0;
    private static final double MAXIMO = 100.0;
    private static final double CHANCE_DE_ENCHARCAR = 0.2;
    private static final double CHANCE_DE_FALHA = 0.03;

    private double ultimaLeitura = 60.0;

    public SensorUmidade(String id, String regiao) {
        super(id, "Sensor de umidade do solo", regiao);
    }

    // O solo encharca com a chuva, senão vai secando
    @Override
    public double lerValor() {
        double variacao = Math.random() * 5 - 2.5;
        if (Math.random() < CHANCE_DE_ENCHARCAR) {
            variacao += 6;
        } else {
            variacao -= 0.8;
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
        return String.format("%s %s - %s - última leitura: %.1f %s",
                getId(), getNome(), getStatus(), ultimaLeitura, getUnidade());
    }

    @Override
    public String getTipo() {
        return UMIDADE;
    }

    @Override
    public String getUnidade() {
        return "%";
    }

    @Override
    public double getUltimaLeitura() {
        return ultimaLeitura;
    }
}
