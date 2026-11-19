package br.com.citizentech.app;

import br.com.citizentech.sensor.Sensor;
import br.com.citizentech.territorio.DadosTerritorio;

public class MenuTerritorio {

    private static final String[] TIPOS_DE_SENSOR = {Sensor.CHUVA, Sensor.NIVEL_RIO, Sensor.UMIDADE};

    private DadosTerritorio dados;

    public MenuTerritorio(DadosTerritorio dados) {
        this.dados = dados;
    }

    public void executar() {
        int opcao;
        do {
            Console.titulo("Dados do território");
            exibirDados();
            System.out.println();
            System.out.println("1. Alterar nome da região");
            System.out.println("2. Alterar população");
            System.out.println("3. Alterar limite de alerta (precisa do código)");
            System.out.println("4. Alterar código de acesso");
            System.out.println("0. Voltar");
            opcao = Console.lerInteiro("Opção", 0, 4);

            // Se a validação recusar, o valor antigo continua igual
            try {
                executarOpcao(opcao);
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage() + " Nada foi alterado.");
            }
        } while (opcao != 0);
    }

    public void exibirDados() {
        System.out.println("Região: " + dados.getNomeRegiao());
        System.out.println("População: " + dados.getPopulacao() + " habitantes");
        System.out.println("Limites de alerta:");
        System.out.println("  " + Sensor.CHUVA + ": " + dados.getLimite(Sensor.CHUVA) + " mm/h");
        System.out.println("  " + Sensor.NIVEL_RIO + ": " + dados.getLimite(Sensor.NIVEL_RIO) + " m");
        System.out.println("  " + Sensor.UMIDADE + ": " + dados.getLimite(Sensor.UMIDADE) + " %");
    }

    private void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                dados.setNomeRegiao(Console.lerTexto("Novo nome"));
                System.out.println("Nome alterado.");
                break;
            case 2:
                dados.setPopulacao(Console.lerInteiro("Nova população", Integer.MIN_VALUE, Integer.MAX_VALUE));
                System.out.println("População alterada.");
                break;
            case 3:
                alterarLimite();
                break;
            case 4:
                String codigoAtual = Console.lerTexto("Código atual");
                dados.alterarCodigoAcesso(codigoAtual, Console.lerTexto("Novo código (4 números)"));
                System.out.println("Código alterado.");
                break;
            default:
                break;
        }
    }

    private void alterarLimite() {
        for (int i = 0; i < TIPOS_DE_SENSOR.length; i++) {
            System.out.println((i + 1) + ". " + TIPOS_DE_SENSOR[i]);
        }
        String tipo = TIPOS_DE_SENSOR[Console.lerInteiro("Sensor", 1, TIPOS_DE_SENSOR.length) - 1];
        double novoLimite = Console.lerDecimal("Novo limite");
        String codigo = Console.lerTexto("Código de acesso");

        dados.alterarLimite(tipo, novoLimite, codigo);
        System.out.println("Limite alterado.");
    }
}
