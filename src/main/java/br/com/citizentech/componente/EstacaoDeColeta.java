package br.com.citizentech.componente;

import br.com.citizentech.sensor.Sensor;

import java.util.ArrayList;
import java.util.List;

public class EstacaoDeColeta extends ComponenteTerritorial {

    private List<Sensor> sensores = new ArrayList<>();

    public EstacaoDeColeta(String id, String nome, String regiao) {
        super(id, nome, regiao);
    }

    public void adicionarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public List<Sensor> getSensores() {
        return new ArrayList<>(sensores);
    }

    @Override
    public String verificarStatus() {
        if (!estaAtivo()) {
            return getNome() + " - DESATIVADA";
        }
        return getNome() + " - ATIVA com " + sensores.size() + " sensores";
    }
}
