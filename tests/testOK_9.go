package main

func main() {
	var arrStr [3]string
	arrStr = [3]string{"b", "b", "c"}
	i := 0
	for i < len(arrStr) {
		println(i, arrStr[i])
		i++
	}
}
