Lista de Compras
Um sistema simples de lista de compras, feito em Java puro, acessível por navegador (inclusive pelo celular, na mesma rede Wi-Fi).

O que o projeto faz
Mostra a lista de produtos a comprar
Permite adicionar novos produtos (evitando duplicados)
Permite esvaziar a lista quando as compras forem feitas
Salva tudo em um arquivo produtos.csv local, sem precisar de banco de dados
Requisitos
A única coisa necessária é o Java (JDK) 17 ou superior instalado. Não precisa de IDE, Maven, Gradle nem nenhuma biblioteca externa — é Java puro.

Pra conferir se o Java já está instalado, abra um terminal (ou "Prompt de Comando" no Windows) e rode:

java -version
Se aparecer um erro ou "comando não encontrado", baixe e instale o Java em: https://adoptium.net/ (gratuito, escolha a versão "LTS" mais recente para o seu sistema operacional).

Como baixar o projeto
Clone o repositório com o Git:

git clone https://github.com/Alelodev/Lista-Compras.git
Ou baixe o .zip direto pelo botão verde "Code" no GitHub e extraia em uma pasta.

Como rodar
Windows
Abra a pasta do projeto
Dê dois cliques no arquivo run.bat
Uma janela preta (terminal) vai abrir, compilar e iniciar o programa automaticamente
Se o Windows bloquear a execução ("Windows protegeu o computador"), clique em "Mais informações" → "Executar assim mesmo".

Linux / Mac
Abra um terminal dentro da pasta do projeto
Dê permissão de execução ao script (só precisa fazer isso uma vez):
chmod +x run.sh
Rode:
./run.sh
Alternativa manual (qualquer sistema operacional)
Se preferir não usar os scripts, dentro da pasta onde estão os arquivos .java rode:

javac *.java
java Main
Como acessar
Depois de rodar, o terminal vai mostrar:

Servidor rodando na porta 8080...
Abra o navegador e acesse:

http://localhost:8080
Deixe essa janela do terminal aberta — fechá-la encerra o servidor.

Acessando de outro dispositivo (celular, outro computador)
O servidor já está configurado para aceitar conexões de qualquer dispositivo na mesma rede Wi-Fi. Para isso:

Descubra o IP local do computador que está rodando o servidor:
Windows: abra o cmd e digite ipconfig — procure por "Endereço IPv4" (algo como 192.168.x.x)
Linux/Mac: abra o terminal e digite ifconfig ou ip a
No celular (conectado na mesma rede Wi-Fi), abra o navegador e acesse:
http://SEU-IP-AQUI:8080
Exemplo: http://192.168.1.10:8080

Observação: o computador precisa estar ligado e com o programa rodando para o celular conseguir acessar. Se o firewall do Windows bloquear a conexão, pode ser necessário liberar a porta 8080 nas configurações do firewall.

Onde os dados ficam salvos
Os produtos são salvos automaticamente no arquivo produtos.csv, criado na mesma pasta onde o programa é executado. Não é necessário criar esse arquivo manualmente — ele é gerado na primeira vez que um produto é adicionado.

Estrutura do projeto
Arquivo	Responsabilidade
Main.java	Ponto de entrada; inicia o servidor
ServidorWeb.java	Define as rotas HTTP e monta as páginas
Menus.java	Lógica de leitura/escrita no CSV
Produto.java	Representa um produto da lista
run.bat / run.sh	Scripts que compilam e rodam o projeto com um único comando
Quer usar uma IDE mesmo assim?
Se preferir abrir o projeto no IntelliJ IDEA (ou outra IDE Java), basta abrir a pasta do projeto e rodar o método main da classe Main.java normalmente — os scripts acima são só um atalho para quem não quer instalar uma IDE.
