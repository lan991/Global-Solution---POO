package br.com.citizentech.sensor;

import br.com.citizentech.componente.ComponenteTerritorial;

public class SensorChuva extends ComponenteTerritorial implements Sensor {

    private static final double MAXIMO = 120.0;
    private static final double CHANCE_DE_PANCADA = 0.15;
    private static final double CHANCE_DE_FALHA = 0.03;

    private double ultimaLeitura = 0.0;

    public SensorChuva(String id, String regiao) {
        super(id, "Pluviômetro", regiao);
    }

    // Às vezes vem uma pancada de chuva, senão a chuva vai diminuindo
    @Override
    public double lerValor() {
        double variacao = Math.random() * 8 - 4;
        if (Math.random() < CHANCE_DE_PANCADA) {
            variacao += 12;
        } else {
            variacao -= 2;
        }
        ultimaLeitura = Math.max(0, Math.min(MAXIMO, ultimaLeitura + variacao));
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
        return CHUVA;
    }

    @Override
    public String getUnidade() {
        return "mm/h";
    }

    @Override
    public double getUltimaLeitura() {
        return ultimaLeitura;
    }
}
