package main

import "fmt"

var a, b int = 1, 2

func fibonacci(i int) int {
	if i == 1 {
		fmt.Println(1, " ")
		return 1
	}
	if i == 2 {
		fmt.Println(1, " ")
		return 1
	}
	return fibonacci(i-2) + fibonacci(i-1)
}

func main() {
	fibonacci(3)
}

// if 2 > 1 {
// 	var a, b, c int = 1, 2, 3
// 	x, y := 1.5, 4.6
// }

// for i := 0; i < 5; i++ {
// 	fmt.Println("foi")
// 	if i == 6/2 {
// 		fmt.Println("Valor de i:", i)
// 	}
// }

// if true {
// 	fmt.Println("foi")
// }
