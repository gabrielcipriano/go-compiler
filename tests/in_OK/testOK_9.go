package main

import "fmt"

func main() {
	valor := 1
	var letras [3]string
	// letras = [3]string{"b", "b", "c"}

	i := 0
	for i <= 3 {
		fmt.Println(i, valor)
		i++
	}
}
