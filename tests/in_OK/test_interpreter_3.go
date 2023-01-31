package main

import "fmt"

func fibonacci(a int, b int, limit int) int {
	next := a + b
	fmt.Println(a)

	if next <= limit {
		return fibonacci(b, next, limit)
	}
	fmt.Println(b)
	return b
}

func fibonacciSequence(limit int) int {
	fmt.Println("Fibonacci sequence:")
	return fibonacci(0, 1, limit)
}

func main() {
	lastFibonacci := fibonacciSequence(30)

	fmt.Println("Last fibonacci before limit:", lastFibonacci)
}
