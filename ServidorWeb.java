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
                    "<a href=\"/esvaziar\">Esvaziar lista</a><br>"
                    ;


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
                        "<a >Digite apenas letras, outros caracteres sao invalidos!</a>" +
                        "<form action=\"/adicionar\" method=\"POST\">" +
                        "<input type=\"text\" name=\"produto\">" +
                        "<button type=\"submit\">Adicionar</button><br>" +
                        "<a href=\"/\">Retornar ao menu</a>" +
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
                    String dados = leitorRequisicao.nextLine().split("=")[1].replace('+', ' ').toUpperCase();
                    corpoRequisicao.close();

                    String resposta;
                    if (!dados.matches("^[a-zA-Z\\s]+$")) {
                        resposta = "Erro: O nome do produto contém caracteres inválidos! Use apenas letras." +
                                "<br><a href=\"/adicionar\">Tentar novamente</a>" +
                                "<br><a href=\"/\">Retornar ao menu</a>";
                    }else if (menu.produtoJaExiste(dados)) {
                        resposta = "Este produto ja esta na lista!" + "<br><a href=\"/\">Retornar ao menu</a>"+
                                "<br><a href=\"adicionar\">Adicione outro produto</a>";
                    } else {
                        menu.salvarProduto(dados);
                        resposta = "Produto " + dados + " adicionado!" +
                                "<br><a href=\"adicionar\">Adicione outro produto</a>" +
                                "<br><a href=\"/\">Retornar ao menu</a>";
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

        servidor.createContext("/lista", (exchange) -> {
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
                        "<button type=\"submit\">apagar</button><br>" +
                        "<a href=\"/\">Retornar ao menu</a>" +
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
                String resposta ="<h1>Lista esvaziada!</h1>" +
                        "<a href=\"/\">Retornar ao menu</a>";
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
                        listaMarcados.add(paresArray[1].replace('+', ' '));
                    }
                }
                corpoRequisicao.close();
                menu.lerMarcador(listaMarcados);

                String resposta = "<h1>Status atualizado!</h1><a href=\"/lista\">Voltar para a lista<br></a><a href=\"/\">Retornar ao menu</a>";
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