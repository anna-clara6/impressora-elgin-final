# Sistema de Impressão com Impressora Elgin

Este sistema demonstra como integrar Java com impressoras térmicas utilizando **JNA (Java Native Access)** para acessar funções presentes na DLL `E1_Impressora01.dll`. O programa oferece um menu interativo que permite configurar a impressora, abrir conexão, imprimir diferentes tipos de dados e utilizar comandos auxiliares.

## Menu e Funções Utilizadas

O sistema utiliza a interface `ImpressoraDLL`, que espelha as funções da DLL e permite acesso direto às operações da impressora. Abaixo estão listadas as funções utilizadas no sistema, agrupadas pela funcionalidade correspondente ao menu.

```java
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA *******************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Canc SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10 - Sinal Sonoro");
            System.out.println("0  - Fechar Conexao e Sair");
            System.out.println("--------------------------------------");
```
---

### Conexão (Menu: 1 e 2)

* `AbreConexaoImpressora(int tipo, String modelo, String conexao, int param)`
  Abre a comunicação com a impressora, permitindo que os comandos seguintes sejam executados. Os parâmetros definem o tipo de conexão (USB, Serial, TCP/IP etc.) e o modelo da impressora.

* `FechaConexaoImpressora()`
  Encerra a comunicação com a impressora, liberando recursos e finalizando o uso da DLL.

---

### Impressão de Conteúdos (Menu: 3, 4, 5)

* `ImpressaoTexto(String dados, int posicao, int estilo, int tamanho)`
  Realiza a impressão de um texto simples, permitindo definir alinhamento, estilo e tamanho da fonte. Ideal para cabeçalhos, informações gerais e testes.

* `ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao)`
  Gera e imprime um QR Code baseado no conteúdo informado. Os parâmetros controlam o tamanho do código e o nível de correção de erro.

* `ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI)`
  Imprime um código de barras no padrão especificado. Permite controlar altura, espessura e exibição de texto legível abaixo do código.

* `AvancaPapel(int linhas)`
  Avança o papel em um número definido de linhas, garantindo espaçamento após a impressão ou preparando o papel para corte.

---

### Impressão de XML SAT (Menu: 6 e 7)

* `ImprimeXMLSAT(String dados, int param)`
  Realiza a impressão estruturada de um arquivo XML SAT, interpretando o documento fiscal e formatando-o conforme o padrão exigido pelo varejo.

* `ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param)`
  Imprime o XML de cancelamento SAT, incluindo a assinatura necessária para validação do QR Code.

* `Corte(int avanco)`
  Executa o corte parcial ou total do papel, dependendo da configuração da impressora, geralmente após a finalização de um documento fiscal.

---

### Sinal Sonoro e Gaveta (Menu: 8, 9 e 10)

* `AbreGavetaElgin()`
  Envia o comando padrão da Elgin para abertura da gaveta de dinheiro acoplada à impressora.

* `AbreGaveta(int pino, int ti, int tf)`
  Permite abertura de gavetas genéricas, escolhendo o pino elétrico e tempos de pulso de ativação.

* `SinalSonoro(int qtd, int tempoInicio, int tempoFim)`
  Emite sinais sonoros na impressora, usados para indicar fim de impressão ou alertas no sistema.

---

## Requisitos Importantes

* A DLL **deve estar no mesmo diretório** da aplicação.
* Java precisa permitir carregamento de bibliotecas nativas.
* Arquivos `XMLSAT.xml` e `CANC_SAT.xml` devem existir na pasta do projeto.
* Drivers da impressora devem estar instalados quando necessário.
