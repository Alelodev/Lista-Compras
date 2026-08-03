import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    Menus( String nomeArquivo) throws IOException {
        this.nomeArquivo = nomeArquivo;
    }
    void salvarProduto(String nomeProduto) throws IOException {
        try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
            writer.write(nomeProduto + "\n");
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
                htmlLista = htmlLista + "<li>" + partes[0] + "</li>";
            }
            htmlLista = "<ul>" + htmlLista + "</ul>";
        }
        return htmlLista;
    }
        void esvaziarArquivo() throws IOException {
            try (FileWriter writer = new FileWriter(nomeArquivo)) { }
        }


    boolean produtoJaExiste(String nomeProduto) throws FileNotFoundException {
        File arquivo = new File(nomeArquivo);
        Scanner leitorArquivo = new Scanner(arquivo);
        while (leitorArquivo.hasNextLine()) {
            String linha = leitorArquivo.nextLine();
            String[] partes = linha.split(";");
            if(partes[0].equals(nomeProduto)){
               return true;
            }

    }
        return false;
    }}


