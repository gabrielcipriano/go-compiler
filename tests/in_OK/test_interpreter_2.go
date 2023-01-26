package main

import "fmt"

var a int = 3

func f(x int) int {
	return x + 1
}

func main() {
	var b int = 4 + a
	var c int = f(b)
	fmt.Println(c)
}
