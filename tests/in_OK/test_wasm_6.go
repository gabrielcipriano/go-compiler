package main

import (
	"fmt"
)

func main() {
	var numeros [5]int

	numeros[0] = 10
	numeros[1] = 200
	numeros[2] = 3000
	numeros[3] = 40000
	numeros[4] = 500000

	for i := 0; i < 5; i++ {
		fmt.Println(numeros[i])
	}

	// var numero int
	// fmt.Scanln(&numero) // scanneia um numero e salva na variavel numero

	// var numeroAleatorio int

	// numeroAleatorio = rand.Intn(100) // gera numero entre 0 e 99

	// tamanho := len(numeros) // length da array

	var deTrasPraFrente [5]int

	for i := 4; i >= 0; i-- {
		deTrasPraFrente[i] = numeros[4-i]
	}

	for i := 0; i < 5; i++ {
		fmt.Println(deTrasPraFrente[i])
	}
}
