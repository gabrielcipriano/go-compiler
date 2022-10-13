// example1.go
package main

/*
	Comentário longo
*/

import (
	"fmt"
	"github.com/antlr/antlr4/runtime/Go/antlr"

	"./parser"
)


// sum 2 numbers
func sum(a int, b int) int {
	return a + b
}

func main() {
	// Setup the input
	is := antlr.NewInputStream("1 + 2 * 3")

	bool := 3 < 4
	bol := 3 <= 5

	num := 3.34

	soma = sum(1, 2)

	// Create the Lexer
	lexer := parser.NewCalcLexer(is)

	// Read all tokens
	for {
		t := lexer.NextToken()
		if t.GetTokenType() == antlr.TokenEOF {
			break
		}
		fmt.Printf("%s (%q)\n",
			lexer.SymbolicNames[t.GetTokenType()], t.GetText())
	}
}
