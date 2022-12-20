package main

func foo(a int, b int) int {
	return 2
}

func main() {
	var a [5]int

	var b int = a[2*2-3]

	var c int = a[6/2-1.0]
}
