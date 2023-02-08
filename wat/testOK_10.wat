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
  
  (func $sum (export "sum") (param $00_a i32) (param $01_b i32) (result i32)    
    
    (local.get $00_a)
    (local.get $01_b)
    (i32.add)
    (return)
  )
)
