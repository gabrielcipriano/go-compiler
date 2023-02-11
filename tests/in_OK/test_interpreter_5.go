package main

import "fmt"

func countdown(start int, end int) int {
	if end > start {
		fmt.Println(start)
		return start
	}

	for i := start; i >= end; i-- {
		fmt.Println(i)
	}
	return end
}

func main() int {
	fmt.Println("contagem regressiva de 10 até 5")
	var rocketLaunch int = countdown(10, 5)

	fmt.Println("contagem regressiva de 5 até 10 (??)")
	countdown(5, 10)

	return rocketLaunch
}
