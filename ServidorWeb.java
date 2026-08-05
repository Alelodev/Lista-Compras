import com.sun.net.httpserver.HttpServer;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ServidorWeb {
    private Menus menu;

    ServidorWeb(Menus menu) {
        this.menu = menu;
    }

    void iniciar() throws IOException {
        HttpServer servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        servidor.createContext("/", (exchange) -> {
            String resposta;
                 resposta = "<h1>Lista de Compras</h1>" +
                        "<a href=\"/lista\">Ver lista</a><br>" +
                        "<a href=\"/adicionar\">Adicionar produto</a><br>" +
                        "<a href=\"/esvaziar\">Esvaziar lista</a>";


            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            OutputStream saida = exchange.getResponseBody();
            saida.write(resposta.getBytes());
            saida.close();

        });
        servidor.createContext("/adicionar", (exchange) -> {
            String metodo = exchange.getRequestMethod();

            if (metodo.equals("GET")) {
                String resposta = "<h1>Lista de Compras</h1>" +
                        "<form action=\"/adicionar\" method=\"POST\">" +
                        "<input type=\"text\" name=\"produto\">" +
                        "<button type=\"submit\">Adicionar</button>" +
                        "</form>";
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, resposta.getBytes().length);
                OutputStream saida = exchange.getResponseBody();
                saida.write(resposta.getBytes());
                saida.close();
            } else {
                try {
                    InputStream corpoRequisicao = exchange.getRequestBody();
                    Scanner leitorRequisicao = new Scanner(corpoRequisicao);
                    String dados = leitorRequisicao.nextLine().split("=")[1].toUpperCase();
                    corpoRequisicao.close();

                    String resposta;
                    if (menu.produtoJaExiste(dados)) {
                        resposta = "Este produto ja esta na lista!";
                    } else {
                        menu.salvarProduto(dados);
                        resposta = "Produto " + dados + " adicionado!";
                    }

                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, resposta.getBytes().length);
                    OutputStream saida = exchange.getResponseBody();
                    saida.write(resposta.getBytes());
                    saida.close();
                } catch (IOException e) {
                    System.out.println("Erro ao processar produto: " + e.getMessage());
                }
            }
        });

        servidor.createContext("/lista", (exchange) ->{
            String resposta;
            try {
                resposta = "<h1>Lista de Compras</h1>" +
                        menu.gerarHtmlLista();
            } catch (FileNotFoundException e) {
                resposta = "<h1>Lista de Compras</h1><p>Erro ao carregar a lista.</p>";
            }
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            OutputStream saida = exchange.getResponseBody();
            saida.write(resposta.getBytes());
            saida.close();
                });

        servidor.createContext("/esvaziar", (exchange) -> {
            String metodo = exchange.getRequestMethod();

            if (metodo.equals("GET")) {
                String resposta = "<form action=\"/esvaziar\" method=\"POST\">" +
                        "<button type=\"submit\">apagar</button>" +
                        "</form>";
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, resposta.getBytes().length);
                OutputStream saida = exchange.getResponseBody();
                saida.write(resposta.getBytes());
                saida.close();
            } else {
                try {
                    menu.esvaziarArquivo();
                } catch (IOException e) {
                    System.out.println("Erro ao esvaziar: " + e.getMessage());
                }
                String resposta = "Lista esvaziada!";
                exchange.sendResponseHeaders(200, resposta.getBytes().length);
                OutputStream saida = exchange.getResponseBody();
                saida.write(resposta.getBytes());
                saida.close();
            }
        });

        servidor.createContext("/marcado", (exchange) -> {
            try {
                InputStream corpoRequisicao = exchange.getRequestBody();
                Scanner leitorRequisicao = new Scanner(corpoRequisicao);
                String dados = "";
                var listaMarcados = new ArrayList<String>();
                if (leitorRequisicao.hasNextLine()) {
                    dados = leitorRequisicao.nextLine();
                    String[] pares = dados.split("&");
                    for (String par : pares) {
                        String[] paresArray = par.split("=");
                        listaMarcados.add(paresArray[1]);
                    }
                }
                corpoRequisicao.close();

                menu.lerMarcador(listaMarcados);

                String resposta = "<h1>Status atualizado!</h1><a href=\"/lista\">Voltar para a lista</a>";
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, resposta.getBytes().length);
                OutputStream saida = exchange.getResponseBody();
                saida.write(resposta.getBytes());
                saida.close();
            } catch (IOException e) {
                System.out.println("Erro ao marcar produtos: " + e.getMessage());
            }
        });



        servidor.start();
        System.out.println("Servidor rodando na porta 8080...");
    }
}