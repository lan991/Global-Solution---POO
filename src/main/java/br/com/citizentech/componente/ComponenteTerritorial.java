package br.com.citizentech.componente;

public abstract class ComponenteTerritorial {

    private String id;
    private String nome;
    private String regiao;
    private StatusComponente status;

    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = StatusComponente.ATIVO;
    }

    public abstract String verificarStatus();

    public void ativar() {
        status = StatusComponente.ATIVO;
    }

    public void desativar() {
        status = StatusComponente.INATIVO;
    }

    public boolean estaAtivo() {
        return status == StatusComponente.ATIVO;
    }

    // Simula uma falha aleatória no equipamento
    protected void testarEquipamento(double chanceDeFalha) {
        if (status == StatusComponente.INATIVO) {
            return;
        }
        if (Math.random() < chanceDeFalha) {
            status = StatusComponente.FALHA;
        } else {
            status = StatusComponente.ATIVO;
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public StatusComponente getStatus() {
        return status;
    }
}
