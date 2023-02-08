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
  
  (global $00_a (mut i32) (i32.const 0))
  
  (global $01_b (mut i32) (i32.const 0))
  
  (global $02_c (mut i32) (i32.const 1))
  
  (global $03_d (mut i32) (i32.const 2))
  
  (global $04_e (mut i32) (i32.const 3))
  
  (func $main (export "main")     
    
    (i32.const 17)
    (global.set $00_a)
  )
)
