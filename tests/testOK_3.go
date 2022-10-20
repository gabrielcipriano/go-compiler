package main

import "fmt"

func main() {
	if true {
		fmt.Println("verdadeiro")
	}

	if false {
		doNothing()
	} else {
		fmt.Println("falso")
	}

	if false {
	} else if 1 > 0 {
		fmt.Println("1 maior que 0")
	} else {
	}
}
