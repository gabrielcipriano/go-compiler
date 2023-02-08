(module 
  ;; Importing std i/o
  (import "std" "printlnInt" (func $printlnInt (param i32)))
  (import "std" "printlnBoolean" (func $printlnBoolean (param i32)))
  (import "std" "printlnFloat" (func $printlnFloat (param f32)))
  (import "std" "printlnString" (func $printlnString (param i32) (param i32)))
  
  ;; creating and exporting memory
  (memory $memory 1)
  (export "memory" (memory $memory))
  
  ;; declaring aux vars
  (global $aux_i32 (mut i32) (i32.const 0))
  (global $aux_f32 (mut f32) (f32.const 0,000000))
  
  ;; adding strings to memory
  (data(i32.const 0) "str")
  (data(i32.const 3) "Hello, World!")
  (data(i32.const 16) "Marcia")
  (data(i32.const 22) "Denira")
  (global $offset (mut i32) (i32.const 28))
  
  (func $printNoix (export "printNoix") (param $00_str i32)     
    
    (local.get $00_str)
    (i32.const 3)
    (call $printlnString)
    (i32.const 1)
    (call $printlnInt)
    (i32.const 0)
    (i32.const 3)
    (call $printlnString)
  )
  
  (func $main (export "main") (result i32)    
    (local $01_tia i32)
    (local $02_vovo i32)
    
    (i32.const 3)
    (i32.const 13)
    (call $printlnString)
    
    (i32.const 0)
    (call $printNoix)
    
    (i32.const 1)
    (i32.const 2)
    (i32.eq)
    (if
      (then
        (i32.const 16)
        (local.set $01_tia)
      )
    )
    
    (i32.const 22)
    (local.set $02_vovo)
  )
)
