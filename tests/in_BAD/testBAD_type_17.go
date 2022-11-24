package main

func foo(a int, b int) (int, int) {
	return a, b
}

func main() {
	var a int = foo(1, 2)
}
