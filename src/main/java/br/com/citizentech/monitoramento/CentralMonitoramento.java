package br.com.citizentech.monitoramento;

import br.com.citizentech.alerta.Alerta;
import br.com.citizentech.alerta.NivelAlerta;
import br.com.citizentech.componente.EstacaoDeColeta;
import br.com.citizentech.previsao.EstrategiaPrevisao;
import br.com.citizentech.sensor.Sensor;
import br.com.citizentech.territorio.DadosTerritorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralMonitoramento {

    private static final int MAXIMO_DE_LEITURAS = 48;

    private DadosTerritorio dados;
    private List<EstacaoDeColeta> estacoes = new ArrayList<>();
    private List<EstrategiaPrevisao> estrategias = new ArrayList<>();
    private Map<String, ArrayList<Double>> historico = new HashMap<>();

    public CentralMonitoramento(DadosTerritorio dados) {
        this.dados = dados;
    }

    public void adicionarEstacao(EstacaoDeColeta estacao) {
        estacoes.add(estacao);
    }

    public void adicionarEstrategia(EstrategiaPrevisao estrategia) {
        estrategias.add(estrategia);
    }

    // Lê todos os sensores que estão funcionando e guarda no histórico
    public void coletarLeituras() {
        for (Sensor sensor : getSensoresEmOperacao()) {
            if (sensor.verificarFuncionamento()) {
                guardarLeitura(sensor.getId(), sensor.lerValor());
            }
        }
    }

    public List<Alerta> gerarAlertas() {
        List<Alerta> alertas = new ArrayList<>();
        for (Sensor sensor : getSensoresEmOperacao()) {
            Alerta alerta = avaliarSensor(sensor);
            if (alerta != null) {
                alertas.add(alerta);
            }
        }
        return alertas;
    }

    // Roda as estratégias e fica com a previsão mais grave
    private Alerta avaliarSensor(Sensor sensor) {
        List<Double> leituras = getLeituras(sensor.getId());
        double limite = getLimite(sensor);
        Alerta maisGrave = null;

        for (EstrategiaPrevisao estrategia : estrategias) {
            if (!estrategia.podePrever(leituras)) {
                continue;
            }
            double previsto = estrategia.prever(leituras);
            NivelAlerta nivel = NivelAlerta.calcular(previsto, limite);

            if (nivel != NivelAlerta.NORMAL && (maisGrave == null || nivel.ehMaisGraveQue(maisGrave.getNivel()))) {
                maisGrave = new Alerta(sensor, nivel, estrategia.getNome(), previsto, limite);
            }
        }
        return maisGrave;
    }

    public boolean temLeiturasSuficientes() {
        for (Sensor sensor : getSensoresEmOperacao()) {
            for (EstrategiaPrevisao estrategia : estrategias) {
                if (estrategia.podePrever(getLeituras(sensor.getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Sensor> getSensoresEmOperacao() {
        List<Sensor> sensores = new ArrayList<>();
        for (EstacaoDeColeta estacao : estacoes) {
            if (estacao.estaAtivo()) {
                sensores.addAll(estacao.getSensores());
            }
        }
        return sensores;
    }

    public EstacaoDeColeta buscarEstacao(String id) {
        for (EstacaoDeColeta estacao : estacoes) {
            if (estacao.getId().equalsIgnoreCase(id)) {
                return estacao;
            }
        }
        return null;
    }

    public List<Double> getLeituras(String idSensor) {
        if (!historico.containsKey(idSensor)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(historico.get(idSensor));
    }

    public double getLimite(Sensor sensor) {
        return dados.getLimite(sensor.getTipo());
    }

    public List<EstacaoDeColeta> getEstacoes() {
        return estacoes;
    }

    public List<EstrategiaPrevisao> getEstrategias() {
        return estrategias;
    }

    private void guardarLeitura(String idSensor, double valor) {
        if (!historico.containsKey(idSensor)) {
            historico.put(idSensor, new ArrayList<Double>());
        }
        ArrayList<Double> leituras = historico.get(idSensor);
        leituras.add(valor);

        // Mantém só as leituras mais recentes
        if (leituras.size() > MAXIMO_DE_LEITURAS) {
            leituras.remove(0);
        }
    }
}
