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
  (global $offset (mut i32) (i32.const 0))
  
  (global $00_b (mut f32) (f32.const 2,500000))
  
  (func $sumTwoInts (export "sumTwoInts") (param $01_num1 i32) (param $02_num2 i32) (result i32)    
    
    (local.get $01_num1)
    (local.get $02_num2)
    (i32.add)
    (return)
  )
  
  (func $main (export "main")     
    (local $03_a i32)
    
    (i32.const 3)
    (i32.const 1)
    (i32.sub)
    (local.set $03_a)
  )
)
