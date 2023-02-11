package main

func doNothingArr(array []int) {

}
func doNothing(num int) int {
	return num
}
func main() {
	var d [5]int

	d[4] = 5
	doNothing(d[4])
	doNothingArr(&d)
}
