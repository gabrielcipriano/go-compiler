package main

import "fmt"

func countdown(val int) int {
	if val < 0 {
		return 0
	}
	fmt.Println(val)
	return countdown(val - 1)
}

func main() int {
	var rocketLaunch int = countdown(10)
	return rocketLaunch
}
