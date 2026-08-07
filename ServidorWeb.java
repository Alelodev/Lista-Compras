import com.sun.net.httpserver.HttpServer;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ServidorWeb {
    private Menus menu;

    ServidorWeb(Menus menu) {
        this.menu = menu;
    }

    private static final String INICIO_HTML = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "<style>" +
            "* { box-sizing: border-box; }" +
            "body { font-family: -apple-system, Segoe UI, Roboto, sans-serif; background: #f4f6f8; color: #222; margin: 0; padding: 20px; display: flex; justify-content: center; }" +
            ".container { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 24px; max-width: 480px; width: 100%; }" +
            "h1 { font-size: 1.5rem; margin-top: 0; color: #2c3e50; }" +
            "a { display: inline-block; color: #2980b9; text-decoration: none; margin: 8px 0; font-size: 1.05rem; }" +
            "a:hover { text-decoration: underline; }" +
            "input[type=\"text\"] { width: 100%; padding: 12px; font-size: 1rem; border: 1px solid #ccc; border-radius: 8px; margin: 12px 0; }" +
            "button { width: 100%; padding: 12px; font-size: 1rem; background: #27ae60; color: white; border: none; border-radius: 8px; cursor: pointer; margin-bottom: 12px; }" +
            "button:hover { background: #219150; }" +
            "ul { list-style: none; padding: 0; margin: 0; }" +
            "li { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid #eee; font-size: 1.05rem; }" +
            "li input[type=\"checkbox\"] { width: 22px; height: 22px; margin-right: 12px; }" +
            "p { font-size: 1.05rem; }" +
            "</style></head><body><div class=\"container\">";

    private static final String FIM_HTML = "</div></body></html>";

    void iniciar() throws IOException {

        HttpServer servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        servidor.setExecutor(Executors.newFixedThreadPool(10));

        servidor.createContext("/", (exchange) -> {
            String resposta = INICIO_HTML +
                    "<h1>Lista de Compras Da Familia AST</h1>" +
                    "<a href=\"/lista\">Ver lista</a><br>" +
                    "<a href=\"/adicionar\">Adicionar produto</a><br>" +
                    "<a href=\"/esvaziar\">Esvaziar lista</a><br>" +
                    FIM_HTML;

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, resposta.getBytes().length);
            OutputStream saida = exchange.getResponseBody();
            saida.write(resposta.getBytes());
            saida.close();
        });

        servidor.createContext("/adicionar", (exchange) -> {
            String metodo = exchange.getRequestMethod();

            if (metodo.equals("GET")) {
                String resposta = INICIO_HTML +
                        "<h1>Lista de Compras Da Familia AST</h1>" +
                        "<p>Digite apenas letras, outros caracteres sao invalidos!</p>" +
                        "<form action=\"/adicionar\" method=\"POST\">" +
                        "<input type=\"text\" name=\"produto\">" +
                        "<button type=\"submit\">Adicionar</button><br>" +
                        "<a href=\"/\">Retornar ao menu</a>" +
                        "</form>" +
                        FIM_HTML;
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
                        resposta = "<p>Erro: O nome do produto contém caracteres inválidos! Use apenas letras.</p>" +
                                "<a href=\"/adicionar\">Tentar novamente</a><br>" +
                                "<a href=\"/\">Retornar ao menu</a>";
                    } else if (menu.produtoJaExiste(dados)) {
                        resposta = "<p>Este produto ja esta na lista!</p>" +
                                "<a href=\"/\">Retornar ao menu</a><br>" +
                                "<a href=\"/adicionar\">Adicione outro produto</a><br"+
                        "<a href=\"/lista\">Ver a Lista</a>";
                    } else {
                        menu.salvarProduto(dados);
                        resposta = "<p>Produto " + dados + " adicionado!</p>" +
                                "<a href=\"/adicionar\">Adicione outro produto</a><br>" +
                                "<a href=\"/\">Retornar ao menu</a><br>"+
                                "<a href=\"/lista\">Ver a Lista</a>";;
                    }

                    resposta = INICIO_HTML + resposta + FIM_HTML;

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
                resposta = INICIO_HTML + "<h1>Lista de Compras Da Familia AST</h1>" + menu.gerarHtmlLista() + FIM_HTML;
            } catch (FileNotFoundException e) {
                resposta = INICIO_HTML + "<h1>Lista de Compras Da Familia AST</h1><p>Erro ao carregar a lista.</p>" + FIM_HTML;
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
                String resposta = INICIO_HTML +
                        "<h1>Esvaziar lista</h1>" +
                        "<form action=\"/esvaziar\" method=\"POST\">" +
                        "<button type=\"submit\">Apagar</button><br>" +
                        "<a href=\"/\">Retornar ao menu</a>" +
                        "</form>" +
                        FIM_HTML;
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
                String resposta = INICIO_HTML +
                        "<h1>Lista esvaziada!</h1>" +
                        "<a href=\"/\">Retornar ao menu</a>" +
                        FIM_HTML;
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
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

                String resposta = INICIO_HTML +
                        "<h1>Status atualizado!</h1>" +
                        "<a href=\"/lista\">Voltar para a lista</a><br>" +
                        "<a href=\"/\">Retornar ao menu</a>" +
                        FIM_HTML;
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