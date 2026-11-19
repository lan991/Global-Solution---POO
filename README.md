# Territory Predictive Insights - Clima local

Global Solution - Programação Orientada a Objetos

Sistema em Java que lê sensores simulados, guarda as últimas leituras, faz previsões simples e emite alertas (`ATENÇÃO`, `ALERTA` e `CRÍTICO`) explicando o motivo de cada um.

**Recorte:** Clima local - chuva forte e risco de alagamento.

## Integrantes

- Luan de Siqueira Pinheiro Santos — RM 567224
- Lucas Alvarenga Blois de Oliveira — RM 567762
- Thiago Cruz da Silva — RM 568232

## Público beneficiado

- **Quem sofre o problema:** moradores da Vila Esperança (bairro fictício), principalmente quem mora na margem do Córrego do Ipê e na encosta da Rua Alta.
- **Quem usa o sistema:** associação de moradores e voluntários da Defesa Civil do bairro.
- **Decisão que o sistema apoia:** se é preciso agir hoje (limpar bueiros, avisar moradores, interditar passagens ou chamar a Defesa Civil).

## Como executar

Precisa do **JDK 17** ou mais novo instalado.

**Pela IDE (IntelliJ, VS Code ou Eclipse):** abrir a pasta do projeto (onde está o `pom.xml`) e rodar a classe `SistemaMonitoramento`, localizada em `src/main/java/br/com/citizentech/app/SistemaMonitoramento.java`.

**Pelo terminal (manual com `javac`):**

```bash
javac -encoding UTF-8 -d target/classes src/main/java/br/com/citizentech/*/*.java
java -cp target/classes br.com.citizentech.app.SistemaMonitoramento
```

### Primeiro uso

1. No começo não tem leituras guardadas, então o sistema ainda não faz previsão.
2. Escolher a opção **5** e simular **24** horas (ou qualquer janela entre 1 e 48 horas).
3. Depois testar as opções **2** (previsões), **3** (alertas) e **6** (status completo).
4. Na opção **4** dá para testar o encapsulamento e as validações (população negativa, código errado, limite fora da faixa, etc.).
5. Código de acesso padrão para alterar limites: **2026**

## Estrutura do projeto

```text
├── pom.xml
├── README.md
└── src/
    └── main/java/br/com/citizentech/
        ├── alerta/         Alerta, NivelAlerta
        ├── app/            Console, MenuTerritorio, SistemaMonitoramento
        ├── componente/     ComponenteTerritorial (abstrata), EstacaoDeColeta, StatusComponente
        ├── monitoramento/  CentralMonitoramento
        ├── previsao/       EstrategiaPrevisao (abstrata), PrevisaoMediaMovel, PrevisaoComparacao
        ├── sensor/         Sensor (interface), SensorChuva, SensorNivelRio, SensorUmidade
        └── territorio/     DadosTerritorio (encapsulamento)
```

## Conceitos de POO

| Conceito | Onde foi aplicado |
| :--- | :--- |
| Classe abstrata | `ComponenteTerritorial` define `id`, `nome`, `regiao` e `status`, os métodos concretos `ativar()` e `desativar()` e o método abstrato `verificarStatus()`. `EstacaoDeColeta` e os sensores herdam dela. |
| Interface | `Sensor` define o contrato com `lerValor()`, `verificarFuncionamento()`, `getTipo()`, `getUnidade()`, entre outros. É implementada por `SensorChuva`, `SensorNivelRio` e `SensorUmidade`. |
| Encapsulamento | Em `DadosTerritorio` todos os atributos são privados. Valores inválidos são recusados com `IllegalArgumentException` e o valor antigo é mantido. Os limites só mudam com o código de acesso. |
| Herança | `PrevisaoMediaMovel` e `PrevisaoComparacao` herdam de `EstrategiaPrevisao`, cada uma com atributo próprio (`tamanhoJanela` e `tamanhoPeriodo`), sobrescrevendo `prever()` e usando `super()`. |

## Como funciona

- **Leitura e histórico:** a cada hora simulada, cada sensor ativo testa se não houve falha aleatória e gera um valor próximo da leitura anterior. A `CentralMonitoramento` guarda as leituras em um `ArrayList` por sensor (mantendo as últimas 48).
- **Previsão por média móvel:** calcula a média das últimas 5 leituras.
- **Previsão por comparação:** compara a média das 3 últimas leituras com as 3 anteriores e projeta a mesma variação.
- **Alertas:** a previsão é comparada com o limite do sensor configurado em `DadosTerritorio`:
  - `ATENÇÃO`: a partir de 80% do limite
  - `ALERTA`: a partir de 100% do limite
  - `CRÍTICO`: a partir de 130% do limite

Quando as duas estratégias geram alerta para o mesmo sensor, o sistema exibe o nível mais grave encontrado, mostrando o sensor, a estratégia utilizada, o valor previsto, o limite e uma recomendação prática.

Limites padrão: chuva `30.0 mm/h`, nível do rio `3.5 m` e umidade do solo `85.0 %`.

## Decisões da equipe

- O recorte de clima local foi escolhido pela coerência direta entre chuva, nível do córrego e umidade do solo na encosta.
- O projeto segue a estrutura padrão Maven (`src/main/java`) para abrir direto em qualquer IDE e compilar sem dependências externas.
- Os sensores herdam de `ComponenteTerritorial` e implementam `Sensor`, reaproveitando id, região, status e simulação de falha.
- Cada leitura simulada parte da leitura anterior para representar uma evolução realista do clima ao longo das horas.
- **Bônus:** implementação da simulação contínua de até 48 horas (opção 5 do menu), exibindo hora a hora as leituras e os alertas gerados ao longo do tempo.

## Reflexão crítica

**Quando o sistema deve se calar?**

Com poucas leituras a previsão não é confiável. A média móvel precisa de 5 leituras e a comparação de períodos precisa de 6; antes disso o sistema avisa que ainda não há dados suficientes em vez de chutar um número. Leituras de sensor com falha ou de estação desativada também são ignoradas. Alerta sem base vira alarme falso e faz as pessoas pararem de confiar no sistema.

**O que os dados não representam?**

Um sensor mede apenas o ponto onde está instalado: pode chover forte na encosta e pouco na margem do córrego. O alagamento também depende de fatores que o sensor não enxerga, como bueiro entupido, lixo na rua ou obra bloqueando a passagem da água. Além disso, as previsões olham só para o histórico recente e não sabem se uma frente fria está chegando, e os valores aqui são simulados para validar a lógica.

**Por que o alerta apoia a decisão em vez de decidir?**

O sistema não sabe se a rua já foi interditada, se há algum morador acamado em área de risco ou se a equipe já fez a limpeza preventiva. Por isso ele explica o motivo do alerta e sugere uma ação, mas a decisão final continua sendo das pessoas responsáveis após conferirem a situação no local.

## Divisão de tarefas

- **Luan de Siqueira Pinheiro Santos (RM 567224):** estruturação do projeto, dados do território (`territorio/`), menus interativos (`app/`) e integração final dos arquivos.
- **Lucas Alvarenga Blois de Oliveira (RM 567762):** componentes territoriais e sensores (`componente/`, `sensor/`).
- **Thiago Cruz da Silva (RM 568232):** estratégias de previsão, central de monitoramento e alertas (`previsao/`, `monitoramento/`, `alerta/`).

## Uso de IA

- **Onde foi usada:** utilizamos IA (Claude e Gemini) como apoio durante o desenvolvimento para tirar dúvidas sobre herança e encapsulamento em Java, organizar melhor a divisão dos pacotes, validar o tratamento de exceções no console e revisar a escrita deste README.
- **Como foi revisado:** após cada integrante implementar e enviar sua parte do sistema, o grupo juntou os módulos, revisou todas as classes para garantir que seguissem os requisitos do enunciado e testou todas as opções do menu e cenários de simulação no terminal.

## O que pode melhorar

- Integrar com dados reais de estações públicas (como INMET ou CEMADEN).
- Fazer o volume de chuva influenciar diretamente a subida do nível do córrego.
- Salvar o histórico de leituras em arquivo para manter os dados entre execuções.