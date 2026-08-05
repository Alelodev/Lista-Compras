import java.io.IOException;

public class Main {
    static void main() throws IOException {
        Menus menu = new Menus("produtos.csv");
        ServidorWeb servidor = new ServidorWeb(menu);
        servidor.iniciar();

    }
}