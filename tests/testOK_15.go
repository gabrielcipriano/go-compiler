package main

import ("bufio" "fmt" "os" "strings")

func main() {

  reader := NewReader(Stdin)
  fmt.Println("Simple Shell")
  fmt.Println("---------------------")

  for {
    fmt.Println("-> ")
    text, _ := ReadString("\n")
    // convert CRLF to LF
    text = Replace(text, "\n", "", -1)

    aaa := "\\"

    if Compare("hi", text) == 0 {
      fmt.Println("hello, Yourself")
    }

  }

}

