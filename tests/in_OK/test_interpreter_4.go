package main

import "fmt"

func gcd(u int, v int) int {
	if (v == 0) return u
	else return gcd(v, u%v)
}

func main() {
	// scan("%d%d", x, y)
	x := 8
	y := 2
	fmt.Println(gcd(x, y))
}
