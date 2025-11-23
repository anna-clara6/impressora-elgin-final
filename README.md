# impressora-elgin-final

## Menu Impressora

O sistema permite a configuração e controle de uma impressora através de comandos que interagem com a DLL específica para a impressora. Abaixo estão listadas as funcionalidades disponíveis no menu:

### Funcionalidades

1. **Configurar Conexão**:  
   Chama `ConfigurarConexao()`.  
   Esta função solicita ao usuário o tipo de conexão, o modelo da impressora e o método de conexão (USB, RS232, etc.). Configura a impressora com essas informações.

2. **Abrir Conexão**:  
   Chama `AbrirConexao()`.  
   A função tenta abrir a conexão com a impressora usando os parâmetros configurados anteriormente (tipo, modelo e conexão).

3. **Impressão Texto**:  
   Chama `ImpressaoTexto(dados, posicao, estilo, tamanho)`.  
   Solicita um texto para impressão e configura o posicionamento, estilo e tamanho do texto a ser impresso.

4. **Impressão QRCode**:  
   Chama `ImpressaoQRCode(dados, tamanho, nivelCorrecao)`.  
   Solicita ao usuário o conteúdo do QR Code, o tamanho e o nível de correção antes de enviá-lo para impressão.

5. **Impressão Código de Barras**:  
   Chama `ImpressaoCodigoBarras(tipo, dados, altura, largura, HRI)`.  
   Solicita o código de barras, o tipo de código, altura, largura e o HRI (Human Readable Interpretation) para gerar e imprimir o código de barras.

6. **Impressão XML SAT**:  
   Chama `ImprimeXMLSAT(xml, param)`.  
   Abre um `JFileChooser` para selecionar um arquivo XML, lê o arquivo e envia para impressão.

7. **Impressão XML Cancelamento SAT**:  
   Chama `ImprimeXMLCancelamentoSAT(xml, assQRCode, param)`.  
   Abre um `JFileChooser` para selecionar o arquivo XML de cancelamento, solicita ao usuário a assinatura do QR Code, e então envia os dados para a impressora.

8. **Abrir Gaveta Elgin**:  
   Chama `AbreGavetaElgin()`.  
   Abre a gaveta de dinheiro Elgin conectada à impressora.

9. **Abrir Gaveta (manual)**:  
   Chama `AbreGaveta(pin, ti, tf)`.  
   Solicita ao usuário o pino de controle, o tempo inicial e final para abrir a gaveta manualmente.

10. **Sinal Sonoro**:  
    Chama `SinalSonoro(qtd, t1, t2)`.  
    Solicita ao usuário a quantidade de bipes (qtd), tempo de início e tempo de fim para emitir um sinal sonoro.

11. **Fechar Conexão e Sair**:  
    Chama `FecharConexao()`.  
    Fecha a conexão com a impressora e encerra o sistema, saindo do loop de opções.

---

## Como Usar

1. Execute o programa.
2. O menu será exibido com as opções numeradas.
3. Digite o número da opção desejada para executar a função correspondente.
4. O programa continuará em execução até que a opção de fechar a conexão e sair seja escolhida.

### Observações
- Certifique-se de que a impressora esteja corretamente conectada antes de abrir a conexão e executar as operações.
- Alguns comandos podem necessitar de parâmetros adicionais, como arquivos XML ou dados para impressão.
- A função de "Abrir Gaveta" depende da impressora estar equipada com gaveta e de a conexão estar ativa.
---
