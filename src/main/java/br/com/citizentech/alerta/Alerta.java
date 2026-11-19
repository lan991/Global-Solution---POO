package br.com.citizentech.alerta;

import br.com.citizentech.sensor.Sensor;

public class Alerta {

    private Sensor sensor;
    private NivelAlerta nivel;
    private String estrategia;
    private double valorPrevisto;
    private double limite;

    public Alerta(Sensor sensor, NivelAlerta nivel, String estrategia, double valorPrevisto, double limite) {
        this.sensor = sensor;
        this.nivel = nivel;
        this.estrategia = estrategia;
        this.valorPrevisto = valorPrevisto;
        this.limite = limite;
    }

    public String explicar() {
        return String.format("[%s] %s %s: a %s prevê %.1f %s e o limite é %.1f %s (%s).",
                nivel.getNome(), sensor.getId(), sensor.getNome(), estrategia,
                valorPrevisto, sensor.getUnidade(), limite, sensor.getUnidade(), nivel.getSignificado());
    }

    public String recomendar() {
        switch (nivel) {
            case ATENCAO:
                return "Ficar de olho, verificar bueiros e a encosta.";
            case ALERTA:
                return "Avisar os moradores das áreas de risco.";
            case CRITICO:
                return "Ligar para a Defesa Civil (199) e retirar os moradores das áreas de risco.";
            default:
                return "Nenhuma ação necessária.";
        }
    }

    public Sensor getSensor() {
        return sensor;
    }

    public NivelAlerta getNivel() {
        return nivel;
    }
}
