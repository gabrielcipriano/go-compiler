package main

import "fmt"

func printArray(arr []int) {
	for i := 0; i < len(arr); i++ {
		fmt.Println(arr[i])
	}
}

func main() {
	var arrayzinha [3]int

	arrayzinha[0] = 1
	arrayzinha[1] = 20
	arrayzinha[2] = 300

	fmt.Println("inicio :)")
	printArray(&arrayzinha)
	fmt.Println("fim :)")
}
