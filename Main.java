import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;

public class Main {

    // Interface que representa a DLL, usando JNA
    public interface ImpressoraDLL extends Library {

        // Caminho completo para a DLL
        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "./E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        public static String lerArquivoComoString(String path) throws IOException {
            FileInputStream fis = new FileInputStream(path);
            byte[] data = fis.readAllBytes();
            fis.close();
            return new String(data, StandardCharsets.UTF_8);
        }

        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);
        int AvancaPapel(int linhas);
        int StatusImpressora(int param);
        int AbreGavetaElgin();
        int AbreGaveta(int pino, int ti, int tf);
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);
        int ModoPagina();
        int LimpaBufferModoPagina();
        int ImprimeModoPagina();
        int ModoPadrao();
        int PosicaoImpressaoHorizontal(int posicao);
        int PosicaoImpressaoVertical(int posicao);
        int ImprimeXMLSAT(String dados, int param);
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro = 0;

    private static final Scanner scanner = new Scanner(System.in);

    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
    
    public static void ConfigurarConexao() {
        // Solicita ao usuário o tipo de conexão e os dados necessários para configurar a impressora.
        System.out.println(
                "Para começar, configure a impressora.\n" +
                        "Primeiro informe o tipo de conexão:\n" +
                        "1 - USB\n" +
                        "2 - RS232\n" +
                        "3 - TCP/IP\n" +
                        "4 - Bluetooth\n" +
                        "5 - Impressoras acopladas (Android)\n"
        );
        tipo = scanner.nextInt();

        while (tipo <= 0 || tipo >= 6) {
            System.out.println("Digite um número entre 1 e 5.");
            tipo = scanner.nextInt();
        }

        if (tipo == 5) {
            modelo = "";
            conexao = "";
        }
        else {
            System.out.println("Informe o modelo: (EX: i7, i8, MP-4200...)");
            modelo = scanner.next();
            System.out.println("Informe a conexão: (EX: USB, RS232, Bluetooth...)");
            conexao = scanner.next();
        }

        System.out.println("Impressora configurada com sucesso!");
    }

    public static void AbrirConexao() {
        // Abre a conexão com a impressora usando os parâmetros configurados anteriormente.
        int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

        if (retorno == 0) {
            System.out.println("Conexão aberta com sucesso!");
            conexaoAberta = true;
        }
        else{
            System.out.println("Erro de conexão. Retorno: " + retorno);
        }
    }

    public static void FecharConexao() {
        // Fecha a conexão com a impressora.
        ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
        System.out.println("Finalizando conexão...");
    }

    private static void ImpressaoTexto() {
        // Verifica se a conexão com a impressora está aberta antes de tentar imprimir.
        if (!conexaoAberta) {
            System.out.println("Erro: A conexão com a impressora não está aberta!");
            System.out.println("Abra a conexão antes de tentar imprimir.\n");
            return;
        }
        
        // Envia um texto à impressora para impressão.
        ImpressoraDLL.INSTANCE.ImpressaoTexto("Teste de impressao", 1, 4, 0);
        
        System.out.println("\nTexto enviado à impressora com sucesso!\n");
    }

    private static void ImpressaoQrCode() {
        // Verifica se a conexão está aberta antes de tentar imprimir o QR Code.
        if (!conexaoAberta) { 
            System.out.println("Abra a conexão primeiro!"); 
            return; 
        }
        
        // Envia um QR Code à impressora.
        int retQR = ImpressoraDLL.INSTANCE.ImpressaoQRCode("Teste de impressao", 6, 4);
        if (retQR == 0) {
            System.out.println("QRCode impresso!");
            return;
        }
        System.out.println("Erro: " + retQR);
    }

    private static void ImpressaoCodBarras() {
        // Verifica se a conexão está aberta antes de tentar imprimir o código de barras.
        if (!conexaoAberta) {
            System.out.println("Erro: A conexão com a impressora deve estar aberta (Opção 2) antes de imprimir.");
            return;
        }

        // Envia um código de barras à impressora.
        int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(8, "{A012345678912", 100, 2, 3);

        if (retorno == 0) {
            System.out.println("\nCódigo de Barras enviado com sucesso! (Retorno: 0)");
            return;
        } 
        System.out.println("\nErro na Impressão. Código de Retorno: " + retorno);
    }

    private static void ImpressaoXMLSAT() {
        // Verifica se a conexão está aberta antes de tentar imprimir o XML SAT.
        if (!conexaoAberta) {
            System.out.println("A Conexão não está aberta!");
            return;
        }
        
        // Caminho do arquivo XML SAT a ser impresso.
        String caminho = "path=./XMLSAT.xml";

        // Imprime o XML SAT na impressora.
        int ret = ImpressoraDLL.INSTANCE.ImprimeXMLSAT(caminho, 0);
        if (ret == 0) {
            System.out.println("Impresso com sucesso!!");
            return;
        }
        System.out.println("Houve um erro ao fazer a impressão xml sat");
    }

    private static void ImpressaoXMLCancSAT() {
        // Verifica se a conexão está aberta antes de tentar imprimir o XML de cancelamento SAT.
        if (!conexaoAberta) {
            System.out.println("A Conexão não está aberta!");
            return;
        }

        // Assinatura do QR Code e caminho do arquivo XML de cancelamento.
        String assQRCode = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";
        String caminho = "path=./CANC_SAT.xml";

        // Imprime o XML de cancelamento SAT.
        int ret = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT(caminho, assQRCode, 0);
        if (ret == 0) {
            System.out.println("Impresso com sucesso!!");
            return;
        }
        System.out.println("Houve um erro ao fazer a impressão XML Canc SAT");
    }

    private static void AbreGavetaElgin() {
        // Verifica se a conexão está aberta antes de tentar abrir a gaveta Elgin.
        if (!conexaoAberta) {
            System.out.println("A Conexão não está aberta!");
            return;
        }

        // Abre a gaveta Elgin.
        int resultado = ImpressoraDLL.INSTANCE.AbreGavetaElgin();

        if (resultado == 0) {
            System.out.println("Gaveta aberta!");
            return;
        }
        System.out.println("Erro ao abrir gaveta!");
    }

    private static void AbreGaveta() {
        // Verifica se a conexão está aberta antes de tentar abrir a gaveta.
        if (!conexaoAberta) {
            System.out.println("A Conexão não está aberta!");
            return;
        }

        // Abre a gaveta com parâmetros adicionais.
        int resultado = ImpressoraDLL.INSTANCE.AbreGaveta(1, 5, 10);

        if (resultado == 0) {
            System.out.println("Gaveta aberta!");
            return;
        }
        System.out.println("Erro ao abrir gaveta!");
    }

    private static void SinalSonoro() {
        // Verifica se a conexão está aberta antes de emitir um sinal sonoro.
        if (!conexaoAberta) {
            System.out.println("A Conexão não está aberta!");
            return;
        }

        // Emite um sinal sonoro.
        int resultado = ImpressoraDLL.INSTANCE.SinalSonoro(4, 5, 5);

        if (resultado == 0) {
            System.out.println("Sinal enviado!");
            return;
        }
        System.out.println("Erro ao emitir o sinal!");
    }

    public static void main(String[] args) {

        while (true) {
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

            String escolha = capturarEntrada("\nDigite a opção desejada: ");

            if (escolha.equals("0")) {
                FecharConexao();
                System.out.println("Encerrando o sistema...");
                break;
            }

            switch (escolha) {
                case "1":
                    ConfigurarConexao(); // Configura a impressora com os dados fornecidos
                    break;

                case "2":
                    AbrirConexao(); // Abre a conexão com a impressora
                    break;

                case "3":
                    ImpressaoTexto(); // Imprime texto
                    ImpressoraDLL.INSTANCE.AvancaPapel(3); // Avança o papel após a impressão
                    break;

                case "4":
                    ImpressaoQrCode(); // Imprime QR Code
                    ImpressoraDLL.INSTANCE.AvancaPapel(3); // Avança o papel após a impressão
                    break;

                case "5":
                    ImpressaoCodBarras(); // Imprime código de barras
                    ImpressoraDLL.INSTANCE.AvancaPapel(3); // Avança o papel após a impressão
                    break;

                case "6":
                    ImpressaoXMLSAT(); // Imprime XML SAT
                    ImpressoraDLL.INSTANCE.AvancaPapel(3); // Avança o papel após a impressão
                    break;

                case "7":
                    ImpressaoXMLCancSAT(); // Imprime XML de cancelamento SAT
                    ImpressoraDLL.INSTANCE.Corte(2); // Realiza o corte após a impressão
                    break;

                case "8":
                    AbreGavetaElgin(); // Abre a gaveta Elgin
                    break;

                case "9":
                    AbreGaveta(); // Abre a gaveta
                    break;

                case "10":
                    SinalSonoro(); // Emite sinal sonoro
                    break;

                default:
                    System.out.println("Opção Inválida! Tente novamente.");
            }
        }

        scanner.close();
    }
}
