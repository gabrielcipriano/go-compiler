# go-compiler

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
- Opera ̧ções de IO básicas sobre stdin e stdout para permitir testes.

## Avaliações

### Lexical

Ter casos de testes, saídas esperadas, e vai rolar perguntas sobre as implementações

### Testes

Teste OK:
  1. Operações matemáticas
  2. Função composta
  3. 

## Dicas

Quando formos ler uma função, pegamos tudo como id e depois lidamos com ela. Utilizando uma tabela de símbolos

```go
func f(int) {}
func 
```
