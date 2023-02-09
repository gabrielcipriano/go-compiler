package main

import (
	"fmt"
	"math/rand"
)
i := 0 

func isSorted(arr []int) bool {

	for i := 1; i < len(arr); i++ {
		if arr[i] < arr[i-1] {
			return false
		}
	}
	return true
}

func shuffle(arr []int) {
	for i := 0; i < len(arr); i++ {
		j := rand.Intn(len(arr) - 1)
		k := rand.Intn(len(arr) - 1)

		aux := arr[j]
		arr[j] = arr[k]
		arr[k] = aux
	}
	i++
	fmt.Println("Ooops...")
	fmt.Println(i)
}

func bogoSort(arr []int) {
	for !isSorted(&arr) {
		shuffle(&arr)
	}
}

func main() {
	// arr := []int{5, 4, 2, 1, 3}
	var arr [9]int

	arr[0] = 54
	arr[1] = 13
	arr[2] = 43
	arr[3] = 23
	arr[4] = 66
	arr[5] = 18
	arr[6] = 96
	arr[7] = 56
	arr[8] = 34

	for i := 0; i < len(arr); i++ {
		fmt.Println(arr[i])
	}

	bogoSort(&arr)

	for i := 0; i < len(arr); i++ {
		fmt.Println(arr[i])
	}
}
