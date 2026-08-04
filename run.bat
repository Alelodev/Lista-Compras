@echo off
echo Compilando o projeto...
javac *.java
if %errorlevel% neq 0 (
    echo Ocorreu um erro ao compilar. Verifique se o Java esta instalado digitando java -version no terminal.
    pause
    exit /b
)
echo Iniciando o servidor...
java Main
pause