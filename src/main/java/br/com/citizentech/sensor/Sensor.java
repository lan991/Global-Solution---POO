package br.com.citizentech.sensor;

public interface Sensor {

    String CHUVA = "Chuva";
    String NIVEL_RIO = "Nível do rio";
    String UMIDADE = "Umidade do solo";

    String getId();

    String getNome();

    String getTipo();

    String getUnidade();

    double lerValor();

    double getUltimaLeitura();

    boolean verificarFuncionamento();

    String verificarStatus();

    boolean estaAtivo();
}
