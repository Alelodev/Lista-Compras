import com.sun.net.httpserver.HttpServer;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
            ":root {" +
            "  --bg-body: #f4f6f8;" +
            "  --bg-container: #fff;" +
            "  --text-color: #222;" +
            "  --h1-color: #2c3e50;" +
            "  --link-color: #2980b9;" +
            "  --border-color: #eee;" +
            "  --input-bg: #fff;" +
            "  --input-border: #ccc;" +
            "}" +
            "[data-theme=\"dark\"] {" +
            "  --bg-body: #121212;" +
            "  --bg-container: #1e1e1e;" +
            "  --text-color: #e0e0e0;" +
            "  --h1-color: #ecf0f1;" +
            "  --link-color: #3498db;" +
            "  --border-color: #333;" +
            "  --input-bg: #2c2c2c;" +
            "  --input-border: #444;" +
            "}" +
            "* { box-sizing: border-box; }" +
            "body { font-family: -apple-system, Segoe UI, Roboto, sans-serif; background: var(--bg-body); color: var(--text-color); margin: 0; padding: 20px; display: flex; justify-content: center; transition: background 0.3s, color 0.3s; }" +
            ".container { background: var(--bg-container); border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 24px; max-width: 480px; width: 100%; transition: background 0.3s; }" +
            "h1 { font-size: 1.5rem; margin-top: 0; color: var(--h1-color); }" +
            "a { display: inline-block; color: var(--link-color); text-decoration: none; margin: 8px 0; font-size: 1.05rem; }" +
            "a:hover { text-decoration: underline; }" +
            "input[type=\"text\"] { width: 100%; padding: 12px; font-size: 1rem; border: 1px solid var(--input-border); border-radius: 8px; margin: 12px 0; background: var(--input-bg); color: var(--text-color); }" +
            "button { width: 100%; padding: 12px; font-size: 1rem; background: #27ae60; color: white; border: none; border-radius: 8px; cursor: pointer; margin-bottom: 12px; }" +
            "button:hover { background: #219150; }" +
            ".btn-theme { background: #7f8c8d; margin-top: 10px; }" +
            ".btn-theme:hover { background: #95a5a6; }" +
            "ul { list-style: none; padding: 0; margin: 0; }" +
            "li { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid var(--border-color); font-size: 1.05rem; }" +
            "li input[type=\"checkbox\"] { width: 22px; height: 22px; margin-right: 12px; }" +
            "p { font-size: 1.05rem; }" +
            "</style>" +
            "<script>" +
            "  (function() {" +
            "    var temaSalvo = localStorage.getItem('tema');" +
            "    if (temaSalvo) {" +
            "      document.documentElement.setAttribute('data-theme', temaSalvo);" +
            "    }" +
            "  })();" +
            "  function alternarTema() {" +
            "    var temaAtual = document.documentElement.getAttribute('data-theme');" +
            "    var novoTema = temaAtual === 'dark' ? 'light' : 'dark';" +
            "    document.documentElement.setAttribute('data-theme', novoTema);" +
            "    localStorage.setItem('tema', novoTema);" +
            "  }" +
            "</script>" +
            "</head><body><div class=\"container\">";

    private static final String FIM_HTML = "</div></body></html>";

    void iniciar() throws IOException {

        HttpServer servidor = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);
        servidor.setExecutor(Executors.newFixedThreadPool(10));

        servidor.createContext("/", (exchange) -> {
            String resposta = INICIO_HTML +
                    "<h1>Lista de Compras Da Familia AST</h1>" +
                    "<a href=\"/lista\">Ver lista</a><br>" +
                    "<a href=\"/adicionar\">Adicionar produto</a><br>" +
                    "<a href=\"/esvaziar\">Esvaziar lista</a><br><br>" +
                    "<button class=\"btn-theme\" onclick=\"alternarTema()\">Alternar Modo Claro/Escuro</button>" +
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
                                "<a href=\"/adicionar\">Adicione outro produto</a><br>"+
                                "<a href=\"/lista\">Ver a Lista</a>";
                    } else {
                        menu.salvarProduto(dados);
                        resposta = "<p>Produto " + dados + " adicionado!</p>" +
                                "<a href=\"/adicionar\">Adicione outro produto</a><br>" +
                                "<a href=\"/\">Retornar ao menu</a><br>"+
                                "<a href=\"/lista\">Ver a Lista</a>";
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