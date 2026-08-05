import java.io.FileWriter;
import java.io.IOException;

public class Main {
    static void main() throws IOException {
        try (FileWriter writer = new FileWriter("produtos.csv", true)) {}
        Menus menu = new Menus("produtos.csv");
        ServidorWeb servidor = new ServidorWeb(menu);
        servidor.iniciar();
    }
}