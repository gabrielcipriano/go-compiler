package main

import "fmt"

func fibonacciIterativo(a int, b int, limit int) int {
	var next int

	for b <= limit {
		fmt.Println(a)
		next = a + b
		a = b
		b = next
	}

	fmt.Println(a)
	return a
}

func fibonacciSequence(limit int) int {
	fmt.Println("Fibonacci sequence:")
	return fibonacciIterativo(0, 1, limit)
}

func main() {
	lastFibonacci := fibonacciSequence(30)

	fmt.Println("Last fibonacci before limit:", lastFibonacci)
}
