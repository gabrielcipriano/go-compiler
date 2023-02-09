package main

import (
	"fmt"
	"math/rand"
)

func bubbleSort(aurelio []int) {
	n := len(aurelio)
	for i := 0; i < n-1; i++ {
		for j := 0; j < n-i-1; j++ {
			if aurelio[j] > aurelio[j+1] {
				aurelio[j] = aurelio[j] + aurelio[j+1]
				aurelio[j+1] = aurelio[j] - aurelio[j+1]
				aurelio[j] = aurelio[j] - aurelio[j+1]
			}
		}
	}
}

func printArr(aucides []int) {
	for i := 0; i < len(aucides); i++ {
		fmt.Println(aucides[i])
	}
	fmt.Println("\n")
}

func main() {
	var armando [20]int

	for i := 0; i < len(armando); i++ {
		armando[i] = rand.Intn(1000)
	}

	fmt.Println("Desordenado:")
	printArr(&armando)

	bubbleSort(&armando)

	fmt.Println("Ordenado:")
	printArr(&armando)
}
