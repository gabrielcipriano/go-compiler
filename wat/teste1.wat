(module
  (import "std" "println_int" (func $println_int (param i32)))
  (import "std" "_print" (func $_print (param i32) (param i32)))
  (import "std" "_println" (func $_println (param i32) (param i32)))

  (export "memory" (memory $mem))
  (memory $mem 1)

  (func $addTwo (export "addTwo") (param i32 i32 ) (result i32)
    local.get 0
    local.get 1
    i32.add
  )

  (data (i32.const 0) "Hello")

  (data (i32.const 5) " World")

  (func (export "main")
    i32.const 3
    i32.const 2
    call $addTwo
    call $println_int
    i32.const 0
    i32.const 5
    call $_print
    i32.const 5
    i32.const 6
    call $_println
  )
)