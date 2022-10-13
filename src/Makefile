# Comando do compilador Java
JAVAC=javac
# Comando da JVM
JAVA=java
# ROOT é a raiz dos diretórios com todos os roteiros de laboratórios
ROOT=/Users/cipriano/git/compiladores
# Caminho para o JAR do ANTLR em labs/tools
ANTLR_PATH=$(ROOT)/tools/antlr-4.11.1-complete.jar
# Opção de configuração do CLASSPATH para o ambiente Java
CLASS_PATH_OPTION=-cp .:$(ANTLR_PATH)
# Configuração do comando de compilação do ANTLR
ANTLR4=$(JAVA) -jar $(ANTLR_PATH)
# Configuração do ambiente de teste do ANTLR
GRUN=$(JAVA) $(CLASS_PATH_OPTION) org.antlr.v4.gui.TestRig
# Nome da gramática
GRAMMAR_NAME=GO
# Diretório para aonde vão os arquivos gerados
GEN_PATH=parser
#
BIN=bin

all: antlr javac
	@echo "Done."
# Executa o ANTLR para compilar a gramática
antlr: $(GRAMMAR_NAME)Lexer.g $(GRAMMAR_NAME)Parser.g
	$(ANTLR4) -no-listener -o $(GEN_PATH) $(GRAMMAR_NAME)Lexer.g $(GRAMMAR_NAME)Parser.g
# Executa o javac para compilar os arquivos gerados
javac:
	$(JAVAC) $(CLASS_PATH_OPTION) $(GEN_PATH)/*.java 
# Executa o parser. Comando: $ make run FILE=arquivo_de_teste
run:
	cd $(GEN_PATH) && $(GRUN) $(GRAMMAR_NAME) program $(FILE) -tree
# Executa o parser. Comando: $ make run FILE=arquivo_de_teste
gui:
	cd $(GEN_PATH) && $(GRUN) $(GRAMMAR_NAME) program $(FILE) -tree -gui
# Remove os arquivos gerados pelo ANTLR
clean:
	@rm -rf $(GEN_PATH)

