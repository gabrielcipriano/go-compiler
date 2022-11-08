package main

import "fmt"

func getWorld() string{
	return "world"
}

func main() {
	var a int
	var b string
	var c float32
	var d bool

	a = 1
	b = "hello" + getWorld() + "!"
	c = 1.0
	d = true

	fmt.Println(a, b, c, d)
}
