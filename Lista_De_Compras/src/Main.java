import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static void main() {

        var leitura = new Scanner(System.in);
        var a = new Produto();
        boolean finalizar = false;
        ArrayList<String> listaDeProdutos = new ArrayList<>();
        int b = 0;
        int caso = 0;
        boolean adicionar = true;

        System.out.println("Qual o seu nome?");
        a.setSeuNome(leitura.nextLine());

        while (!finalizar) {
            switch (caso) {
                case 0:
                    while (true) {
                        b = 0;
                        System.out.println(" ");
                        System.out.println("Ola " + a.getSeuNome() + " oque deseja fazer?");
                        System.out.println("Opcoes: ");
                        System.out.println("1- Ver lista");
                        System.out.println("2- Adicionar produtos a lista");
                        System.out.println("3- Limpar lista");
                        System.out.println("4- Finalizar programa");
                        caso = leitura.nextInt();
                        adicionar = true;
                        if (caso != 1 && caso != 2 && caso != 3 && caso != 4) {
                            System.out.println("operacao invalida!");
                            caso = 0;
                            break;
                        }else {
                            adicionar = true;
                            break;
                        }
                    }
                    break;
                case 1:
                    if (listaDeProdutos.isEmpty()){
                    System.out.println("A lista esta vazia!");
                    caso = 0;
                    break;
                } else{
                    System.out.println("Lista de produtos: " + listaDeProdutos);
                    System.out.println("Deseja retornar ao menu? ou deseja rever a lista?");
                    System.out.println("Digite '1' para retornar ao menu");
                    System.out.println("Digite '2' para rever a lista");
                    b = leitura.nextInt();
                    if (b == 1) {
                        caso = 0;
                        adicionar = false;
                        break;
                    } else {
                        break;
                    }
                }

                case 2:
                    while (adicionar) {
                        leitura.nextLine();
                        System.out.println("Qual produto deseja adicionar a lista? ");
                        a.setNomeProduto(leitura.nextLine());
                        listaDeProdutos.add(a.getNomeProduto());
                        System.out.println("Produto " + a.getNomeProduto() + " foi adicionado a lista!");
                        System.out.println(" ");
                        System.out.println("Deseja adicionar mais produtos a lista? ");
                        System.out.println("digite '1' para sim");
                        System.out.println("digite '2' para nao e retornar ao menu");
                        b = leitura.nextInt();
                        if (b == 2) {
                            caso = 0;
                            adicionar = false;
                            break;
                        }else if(b != 1){
                            System.out.println("Entrada invalida!");
                            System.out.println("Digite '1' para adicionar mais produtos");
                            System.out.println("Digite '2' para voltar ao menu");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Voce tem certeza que deseja esvaziar a lista?");
                    System.out.println("Digite '1' para confirmar");
                    System.out.println("Digite '2' para cancelar");
                    b = leitura.nextInt();
                    if(b == 1){
                        listaDeProdutos.clear();
                        System.out.println("A lista foi esvaziada!");
                        caso = 0;
                        break;
                    } else {
                        System.out.println("A lista nao foi esvaziada");
                        caso = 0;
                        break;
                    }
                case 4:
                        finalizar = true;
                        break;
                    }
            }
        }
    }
