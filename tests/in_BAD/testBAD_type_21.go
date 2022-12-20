package main

func foo(a int, b int) int {
	return 2
}

func main() {
	var a, b int = 1, 2

	if a < 3 {
		a = b
	}

	if 1 + b {
		b = a
	}
}
