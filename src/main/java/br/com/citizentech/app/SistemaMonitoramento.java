package br.com.citizentech.app;

import br.com.citizentech.alerta.Alerta;
import br.com.citizentech.alerta.NivelAlerta;
import br.com.citizentech.componente.EstacaoDeColeta;
import br.com.citizentech.monitoramento.CentralMonitoramento;
import br.com.citizentech.previsao.EstrategiaPrevisao;
import br.com.citizentech.previsao.PrevisaoComparacao;
import br.com.citizentech.previsao.PrevisaoMediaMovel;
import br.com.citizentech.sensor.Sensor;
import br.com.citizentech.sensor.SensorChuva;
import br.com.citizentech.sensor.SensorNivelRio;
import br.com.citizentech.sensor.SensorUmidade;
import br.com.citizentech.territorio.DadosTerritorio;

import java.util.List;

public class SistemaMonitoramento {

    private CentralMonitoramento central;
    private MenuTerritorio menuTerritorio;

    public SistemaMonitoramento() {
        DadosTerritorio dados = new DadosTerritorio("Vila Esperança", 18400, "2026");
        central = new CentralMonitoramento(dados);
        menuTerritorio = new MenuTerritorio(dados);

        EstacaoDeColeta corrego = new EstacaoDeColeta("EST-01", "Estação do Córrego", "Margem do Córrego do Ipê");
        corrego.adicionarSensor(new SensorChuva("PLU-01", "Margem do Córrego do Ipê"));
        corrego.adicionarSensor(new SensorNivelRio("NIV-01", "Margem do Córrego do Ipê"));

        EstacaoDeColeta encosta = new EstacaoDeColeta("EST-02", "Estação da Encosta", "Encosta da Rua Alta");
        encosta.adicionarSensor(new SensorChuva("PLU-02", "Encosta da Rua Alta"));
        encosta.adicionarSensor(new SensorUmidade("UMI-01", "Encosta da Rua Alta"));

        central.adicionarEstacao(corrego);
        central.adicionarEstacao(encosta);
        central.adicionarEstrategia(new PrevisaoMediaMovel(5));
        central.adicionarEstrategia(new PrevisaoComparacao(3));
    }

    public static void main(String[] args) {
        new SistemaMonitoramento().executar();
    }

    public void executar() {
        int opcao;
        do {
            Console.titulo("Territory Predictive Insights - Clima local");
            System.out.println("1. Verificar sensores");
            System.out.println("2. Rodar previsões");
            System.out.println("3. Ver alertas");
            System.out.println("4. Gerenciar dados do território");
            System.out.println("5. Simular próximas leituras");
            System.out.println("6. Exibir status completo");
            System.out.println("0. Sair");
            opcao = Console.lerInteiro("Escolha uma opção", 0, 6);

            switch (opcao) {
                case 1:
                    verificarSensores();
                    break;
                case 2:
                    rodarPrevisoes();
                    break;
                case 3:
                    exibirAlertas();
                    break;
                case 4:
                    menuTerritorio.executar();
                    break;
                case 5:
                    simularLeituras();
                    break;
                case 6:
                    exibirStatusCompleto();
                    break;
                default:
                    System.out.println("Sistema encerrado.");
            }
        } while (opcao != 0);
    }

    private void verificarSensores() {
        Console.titulo("Verificar sensores");
        for (EstacaoDeColeta estacao : central.getEstacoes()) {
            System.out.println();
            System.out.println(estacao.getId() + " " + estacao.verificarStatus());
            if (!estacao.estaAtivo()) {
                continue;
            }
            for (Sensor sensor : estacao.getSensores()) {
                sensor.verificarFuncionamento();
                System.out.println("  " + sensor.verificarStatus() + " | " + verificarLimite(sensor));
            }
        }

        String id = Console.lerTexto("\nDigite o ID de uma estação para ativar/desativar (ou Enter para voltar)");
        if (!id.isEmpty()) {
            alternarEstacao(id);
        }
    }

    private String verificarLimite(Sensor sensor) {
        double limite = central.getLimite(sensor);
        if (sensor.getUltimaLeitura() >= limite) {
            return "ACIMA DO LIMITE de " + limite + " " + sensor.getUnidade();
        }
        return "OK (limite " + limite + " " + sensor.getUnidade() + ")";
    }

    private void alternarEstacao(String id) {
        EstacaoDeColeta estacao = central.buscarEstacao(id);
        if (estacao == null) {
            System.out.println("Estação não encontrada.");
        } else if (estacao.estaAtivo()) {
            estacao.desativar();
            System.out.println(estacao.getNome() + " desativada.");
        } else {
            estacao.ativar();
            System.out.println(estacao.getNome() + " ativada.");
        }
    }

    private void rodarPrevisoes() {
        Console.titulo("Previsões");
        for (Sensor sensor : central.getSensoresEmOperacao()) {
            List<Double> leituras = central.getLeituras(sensor.getId());
            double limite = central.getLimite(sensor);
            System.out.println();
            System.out.println(sensor.getId() + " " + sensor.getNome() + " - " + leituras.size()
                    + " leituras - limite " + limite + " " + sensor.getUnidade());

            for (EstrategiaPrevisao estrategia : central.getEstrategias()) {
                System.out.println("  " + estrategia.getNome() + ": " + preverComSignificado(estrategia, leituras, sensor, limite));
            }
        }
        exibirAlertas();
    }

    private String preverComSignificado(EstrategiaPrevisao estrategia, List<Double> leituras, Sensor sensor, double limite) {
        if (!estrategia.podePrever(leituras)) {
            return "sem leituras suficientes (" + leituras.size() + " de " + estrategia.getMinimoDeLeituras() + ")";
        }
        double previsto = estrategia.prever(leituras);
        NivelAlerta nivel = NivelAlerta.calcular(previsto, limite);
        return String.format("%.1f %s -> %s (%s)", previsto, sensor.getUnidade(), nivel.getNome(), nivel.getSignificado());
    }

    private void exibirAlertas() {
        Console.titulo("Alertas");
        if (!central.temLeiturasSuficientes()) {
            System.out.println("Ainda não há leituras suficientes para prever. Use a opção 5 para simular.");
            return;
        }

        List<Alerta> alertas = central.gerarAlertas();
        if (alertas.isEmpty()) {
            System.out.println("Nenhum alerta no momento.");
            return;
        }
        for (Alerta alerta : alertas) {
            System.out.println(alerta.explicar());
            System.out.println("  O que fazer: " + alerta.recomendar());
        }
        System.out.println("Obs: o alerta ajuda na decisão, mas a situação deve ser conferida no local.");
    }

    private void simularLeituras() {
        Console.titulo("Simulação");
        int horas = Console.lerInteiro("Quantas horas deseja simular (1 a 48)", 1, 48);

        for (int hora = 1; hora <= horas; hora++) {
            central.coletarLeituras();
            System.out.println(String.format("Hora %02d | %s", hora, montarLinhaDeLeituras()));
            System.out.println("        -> " + resumirAlertas());
        }
        exibirAlertas();
    }

    private String montarLinhaDeLeituras() {
        StringBuilder linha = new StringBuilder();
        for (Sensor sensor : central.getSensoresEmOperacao()) {
            if (sensor.estaAtivo()) {
                linha.append(String.format("%s: %.1f %s | ", sensor.getId(), sensor.getUltimaLeitura(), sensor.getUnidade()));
            } else {
                linha.append(sensor.getId()).append(": FALHA | ");
            }
        }
        return linha.toString();
    }

    private String resumirAlertas() {
        if (!central.temLeiturasSuficientes()) {
            return "poucas leituras, sem previsão ainda";
        }
        List<Alerta> alertas = central.gerarAlertas();
        if (alertas.isEmpty()) {
            return "sem alertas";
        }
        StringBuilder resumo = new StringBuilder();
        for (Alerta alerta : alertas) {
            resumo.append(alerta.getNivel().getNome()).append(" em ").append(alerta.getSensor().getId()).append("  ");
        }
        return resumo.toString();
    }

    private void exibirStatusCompleto() {
        Console.titulo("Status completo");
        menuTerritorio.exibirDados();

        System.out.println();
        System.out.println("Estações e sensores:");
        for (EstacaoDeColeta estacao : central.getEstacoes()) {
            System.out.println(estacao.getId() + " " + estacao.verificarStatus());
            for (Sensor sensor : estacao.getSensores()) {
                int quantidade = central.getLeituras(sensor.getId()).size();
                System.out.println("  " + sensor.verificarStatus() + " | " + quantidade + " leituras guardadas");
            }
        }

        System.out.println();
        System.out.println("Estratégias de previsão:");
        for (EstrategiaPrevisao estrategia : central.getEstrategias()) {
            System.out.println("  " + estrategia.descrever());
        }
        exibirAlertas();
    }
}
