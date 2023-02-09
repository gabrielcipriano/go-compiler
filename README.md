# go-compiler
...
A go compiler usign ANTLR4 as frontend

Segue abaixo uma lista mínima de elementos que o seu compilador deve tratar corretamente: [link da especificação](https://drive.google.com/file/d/1Ku9tgrp-aOVd1E94wm8Z764ovLIV9VsK/view)

- Operações aritméticas e de comparação básicas (+, ∗, <, ==, etc).
- Comandos de atribuição.
- Execução de blocos sequenciais de código.
- Pelo menos uma estrutura de escolha (if-then-else) e uma de repetição (while, for, etc).
- Declaração e manipulação de tipos básicos como int, real, string e bool (quando aplicável à LP).
- Declaração e manipulação de pelo menos um tipo composto (vetores, listas em Python, etc).
- Declaração e execução correta de chamadas de função com número de parâmetros fixos (não precisa ser varargs).
- Sistema de tipos que trata adequadamente todos os tipos permitidos.
- Operações de IO básicas sobre stdin e stdout para permitir testes.

## Como executar

Para buildar o projeto, basta executar o comando abaixo:

```bash
make
```

Para executar a validação léxica, sintática e semântica de um teste em específico, e gerar o código wat dele, basta executar o comando abaixo:

```bash
make run FILE=<caminho_do_arquivo>
```

No caso dos nossos testes, o caminho do arquivo é `../tests/<nome_do_arquivo>.go`

Para gerar o código webassembly em forma leitura S, basta executar o comando abaixo:

```bash
make exec FILE=<caminho_do_arquivo>
```
No caso dos nossos testes, o caminho do arquivo é `../wat/<nome_do_arquivo>.go`

Mas para gerar o código wat na pasta wat a partir da leitura do código em go, basta executar o comando abaixo:

```bash
make build-wat FILE=../tests/in_OK/test_wasm_1.go
```
