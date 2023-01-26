package main

import "fmt"

func soma(c float32, d int) float32 {
	return c + d
}

func main() {
	var c float32 // 5
	var d int = 3

	// ...
	c = soma(c, 1)

	fmt.Println(c, soma(d, d))
}
