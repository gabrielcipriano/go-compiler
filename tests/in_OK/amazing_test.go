package main

import (
	"fmt"
	"math/rand"
)

func bubbleSort(array []int) {
	n := len(array)
	for i := 0; i < n-1; i++ {
		for j := 0; j < n-i-1; j++ {
			if array[j] > array[j+1] {
				aux := array[j]
				array[j] = array[j+1]
				array[j+1] = aux
			}
		}
	}
}

func printArr(arr []int) {
	for i := 0; i < len(arr); i++ {
		fmt.Println(arr[i])
	}
	fmt.Println("\n")
}

func main() {
	var randArray [10]int

	for i := 0; i < len(randArray); i++ {
		randArray[i] = rand.Intn(1000)
	}

	fmt.Println("Unordered:")
	printArr(&randArray)

	// inplace
	bubbleSort(&randArray)

	fmt.Println("Ordered:")
	printArr(&randArray)
}
