(module
  (import "std" "printLnInt" (func $printLnInt (param i32)))
  (import "std" "printLnStr" (func $printLnStr (param i32) (param i32)))

  (memory $mem 1)
  (export "memory" (memory $mem))

  (func $addTwo (export "addTwo") (param $a i32) (param $b i32) (result i32)
    local.get $a
    local.get $b
    i32.add
  )

  (data (i32.const 0) "Hello")

  (data (i32.const 5) " World")

  (data (i32.const 11) 123456)

  (func $main (export "main")
    i32.const 3
    i32.const 2
    call $addTwo
    call $printLnInt
    i32.const 0
    i32.const 11
    call $printLnStr
    ;; i32.const 0
    ;; i32.const 5
    ;; call $_print
    ;; i32.const 5
    ;; i32.const 6
    ;; call $_println
  )
)