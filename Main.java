import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static void main() throws IOException {

        var leitura = new Scanner(System.in);

        boolean finalizar = false;
        ArrayList<Produto> listaDeProdutos = new ArrayList<>();
        int caso = 0;
        Menus menu = new Menus(listaDeProdutos, leitura, "produtos.csv");
        System.out.println("Qual o seu nome?");
        menu.setSeuNome(leitura.nextLine());

        while (!finalizar) {
            switch (caso) {
                case 0:
                    caso = menu.exibirMenu();
                    break;
                case 1:
                    caso = menu.verLista();
                    break;
                case 2:
                    caso = menu.adicionarProdutosLista();
                    break;
                case 3:
                    caso = menu.esvaziarLista();
                    break;
                case 4:
                    finalizar = true;
                    break;
            }
        }
    }
}
