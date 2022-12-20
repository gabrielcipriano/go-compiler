package main

import "fmt"

func main() {
	var minhaArray [10]int
	minhaArray[1] = 5
	fmt.Println(minhaArray[1])
	minhaArray[0] = minhaArray[1]
}
