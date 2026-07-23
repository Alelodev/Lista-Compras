import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Menus {
    private Scanner leitura;
    private ArrayList<Produto> listaDeProdutos;
    private String seuNome;
    private String nomeArquivo;

    public String getSeuNome() {
        return seuNome;
    }

    public void setSeuNome(String seuNome) {
        this.seuNome = seuNome;
    }

    Menus(ArrayList<Produto> lista, Scanner leitura, String nomeArquivo) throws IOException {
        this.leitura = leitura;
        this.listaDeProdutos = lista;
        this.nomeArquivo = nomeArquivo;
    }

    int exibirMenu(){
        while (true) {
            int b = 0;
            int caso = 0;
            System.out.println(" ");
            System.out.println("Ola " + this.getSeuNome() + " oque deseja fazer?");
            System.out.println("Opcoes: ");
            System.out.println("1- Ver lista");
            System.out.println("2- Adicionar produtos a lista");
            System.out.println("3- Limpar lista");
            System.out.println("4- Finalizar programa");
            caso = leitura.nextInt();
            if (caso != 1 && caso != 2 && caso != 3 && caso != 4) {
                System.out.println("operacao invalida!");
                return exibirMenu();
            }else {
                return caso;
            }
        }
    }
    int verLista(){
        int b = 0;
        if (listaDeProdutos.isEmpty()){
            System.out.println("A lista esta vazia!");
            return 0;
        } else{
            System.out.println("Lista de produtos: " + listaDeProdutos);
            System.out.println("Deseja retornar ao menu? ou deseja rever a lista?");
            System.out.println("Digite '1' para retornar ao menu");
            System.out.println("Digite '2' para rever a lista");
            b = leitura.nextInt();
            if (b == 1) {
                return 0;
            } else {
                return verLista();
            }
    }}
    int adicionarProdutosLista(){
        boolean adicionar = true;
        while (adicionar) {
            int b = 0;
            Produto a = new Produto();
            leitura.nextLine();
            System.out.println("Qual produto deseja adicionar a lista? ");
            a.setNomeProduto(leitura.nextLine());
            a.setAdicionadoPor(getSeuNome());
            listaDeProdutos.add(a);
            System.out.println("Produto " + a.getNomeProduto() + " foi adicionado a lista!");

            try (FileWriter writer = new FileWriter(nomeArquivo, true)) {
                writer.write(a.getNomeProduto() + ";" + a.getAdicionadoPor() + "\n");
            } catch (IOException e) {
                System.out.println("Erro ao salvar no arquivo: " + e.getMessage());
            }
            System.out.println(" ");
            System.out.println("Deseja adicionar mais produtos a lista? ");
            System.out.println("digite '1' para sim");
            System.out.println("digite '2' para nao e retornar ao menu");
            b = leitura.nextInt();
            if (b == 2) {
                return 0;

            }else if(b != 1){
                System.out.println("Entrada invalida!");
                System.out.println("Digite '1' para adicionar mais produtos");
                System.out.println("Digite '2' para voltar ao menu");
            }
        }
        return 0;
    }
    int esvaziarLista(){
        int b = 0;
        System.out.println("Voce tem certeza que deseja esvaziar a lista?");
        System.out.println("Digite '1' para confirmar");
        System.out.println("Digite '2' para cancelar");
        b = leitura.nextInt();
        if(b == 1){
            listaDeProdutos.clear();
            System.out.println("A lista foi esvaziada!");
            return 0;

        } else {
            System.out.println("A lista nao foi esvaziada");
            return 0;

        }
    }
}
