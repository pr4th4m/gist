package main

import (
	"fmt"
)

type person struct {
    name string
    age  int
}

func main() {

	s := person{name: "Sean", age: 50}
	fmt.Printf("%p\n", &s)
	fmt.Println(s.name)

	sp := &s
	fmt.Println(sp.age)

	sp.age = 51
	fmt.Println(sp.age)
	fmt.Println(s.age)
	fmt.Printf("%p\n", sp)
	fmt.Printf("it works fine")

}
