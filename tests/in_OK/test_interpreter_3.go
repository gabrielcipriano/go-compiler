package main

import "fmt"

var counter int = 0

func somaUmPrint(a int) {
	newA := a + 1
	fmt.Println("valor: ", newA)
}

func main() {
	for i := 0; i < 10; i++ {
		somaUmPrint(i)
		counter++
	}

	fmt.Println("novo valor de counter:", counter)
}
