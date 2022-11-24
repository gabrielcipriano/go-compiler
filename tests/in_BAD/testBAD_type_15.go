package main

func sum(a int, b int) (int, int) {
	return a, b
}

func main() {
	var a int
	a = 3 + sum(a, a)
}
