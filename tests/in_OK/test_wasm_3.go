package main

import "fmt"

func main() {
	var gerim string = "rogerio"
	var nome string = "gabriel"

	if 1 == 2 {
		var a, b float32 = 1, 2
	} else if false || false {
		var a, c float32 = 1, 2
		a = 4.5
	} else {
		var a int = 1
		a = 10
		fmt.Println(a)
	}

	fmt.Println(gerim)
}
