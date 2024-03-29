# Comando do compilador Java
JAVAC=javac
# Comando da JVM
JAVA=java
# ROOT é a raiz dos diretórios com todos os roteiros de laboratórios
ROOT=/Users/cipriano/git/go-compiler
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
LEXER_FILE=$(GRAMMAR_NAME)Lexer.g
PARSER_FILE=$(GRAMMAR_NAME)Parser.g
# Diretório para aonde vão os arquivos gerados
GEN_PATH=parser
# Diretório para os arquivos .class
BIN_PATH=bin

# Executa o ANTLR e o compilador Java
all: antlr javac
	@echo "Done."
# Executa o ANTLR para compilar a gramática
antlr: $(LEXER_FILE) $(PARSER_FILE)
	$(ANTLR4) -no-listener -visitor -o $(GEN_PATH) $(LEXER_FILE) $(PARSER_FILE)
# Compila todos os subdiretórios e joga todos os .class em BIN_PATH pra organizar
javac:
	rm -rf $(BIN_PATH)
	mkdir $(BIN_PATH)
	$(JAVAC) $(CLASS_PATH_OPTION) -d $(BIN_PATH) */*.java Main.java
# Executa o front-end. Comando: $ make run FILE=arquivo_de_teste
# 'Main' é a classe com a função principal
run:
	DEBUG=$(DEBUG) $(JAVA) $(CLASS_PATH_OPTION):$(BIN_PATH) Main $(FILE)
# Executa o TestRig para exibir a parse tree
debug:
	cd $(BIN_PATH) && $(GRUN) $(GEN_PATH).$(GRAMMAR_NAME) program -gui $(FILE)

# Gera o arquivo .wat a partir do arquivo .go
build-wat:
	@sh ./build-wat.sh $(FILE)

exec:
	npm --prefix ../runtime run exec -- $(FILE)
# Remove os arquivos gerados pelo ANTLR
clean:
	@rm -rf $(GEN_PATH) $(BIN_PATH)
