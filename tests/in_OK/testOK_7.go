package main

func len(arr [5]int) int{
	return 5
}

func main() {
	var my_array [5]int = [5]int{1, 2, 3, 4, 5}
	var i int
	for i = 0; i < len(my_array); i++ {
		fmt.Println(my_array[i])
	}

}