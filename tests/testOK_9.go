package main

func main() {
	valor := 1
	var letras [3]string
	letras = [3]string{"b", "b", "c"}
	letras[0], valor = "a", 5+1

	i := 0
	for i < len(letras) {
		println(i, letras[i], valor)
		i++
	}
}
