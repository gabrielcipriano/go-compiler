package main

import "fmt"

func main() {
	var arr [10]int
	arr[1] = 5
	fmt.Println(arr[1])
	arr[0] = arr[1]
}
