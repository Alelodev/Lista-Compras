import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Menus {
    private String seuNome;
    private String nomeArquivo;

    public String getSeuNome() {
        return seuNome;
    }

    public void setSeuNome(String seuNome) {
        this.seuNome = seuNome;
    }

    Menus(String nomeArquivo) throws IOException {
        this.nomeArquivo = nomeArquivo;
    }

    void salvarProduto(String nomeProduto) throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
            writer.write(nomeProduto + ";NAO" + "\n");
        }
    }

    String gerarHtmlLista() throws FileNotFoundException {
        File arquivo = new File(nomeArquivo);
        String htmlLista = "";
        if (arquivo.length() == 0) {
            htmlLista = "<p>A lista esta vazia!</p>";
        } else {
            Scanner leitorArquivo = new Scanner(arquivo);
            while (leitorArquivo.hasNextLine()) {
                String linha = leitorArquivo.nextLine();
                String[] partes = linha.split(";");
                String nomeProduto = partes[0];
                String status = partes[1];
                String checked;
                if (status.equals("SIM")) {
                    checked = "checked";
                } else {
                    checked = "";
                }
                htmlLista = htmlLista + "<li><input type=\"checkbox\" name=\"produtosComprados\" value=\"" + nomeProduto + "\" " + checked + "> " + nomeProduto + "</li>";
            }
            htmlLista = "<form action=\"/marcado\" method=\"POST\">" + "<ul>" + htmlLista + "</ul>" + "<button type=\"submit\">Salvar alterações</button>" + "</form>";
        }
        return htmlLista;
    }

    void esvaziarArquivo() throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
        }
    }

    boolean produtoJaExiste(String nomeProduto) throws FileNotFoundException {
        File arquivo = new File(nomeArquivo);
        Scanner leitorArquivo = new Scanner(arquivo);
        while (leitorArquivo.hasNextLine()) {
            String linha = leitorArquivo.nextLine();
            String[] partes = linha.split(";");
            if (partes[0].equals(nomeProduto)) {
                return true;
            }

        }
        return false;
    }

    void lerMarcador(ArrayList<String> listaMarcados) throws IOException {
        File arquivo = new File(nomeArquivo);
        Scanner leitorArquivo = new Scanner(arquivo);
        var listaParaUsarDepois = new ArrayList<String>();
        while (leitorArquivo.hasNextLine()) {
            String linha = leitorArquivo.nextLine();
            String[] partes = linha.split(";");
            String nomeProduto = partes[0];
            String novoStatus;
            if (listaMarcados.contains(nomeProduto)) {
                novoStatus = "SIM";
            } else {
                novoStatus = "NAO";
            }
            listaParaUsarDepois.add(nomeProduto + ";" + novoStatus);
        }
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (String linhaA : listaParaUsarDepois) {
                writer.write(linhaA + "\n");
            }
        }
    }
}




