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
  
  (global $00_a (mut i32) (i32.const 3))
  
  (func $f (export "f") (param $01_x i32) (result i32)    
    
    (local.get $01_x)
    (i32.const 1)
    (i32.add)
    (return)
  )
  
  (func $main (export "main")     
    (local $02_b i32)
    (local $03_c i32)
    
    (i32.const 4)
    (global.get $00_a)
    (i32.add)
    (local.set $02_b)
    
    (local.get $02_b)
    (call $f)
    (local.set $03_c)
    
    (local.get $03_c)
    (call $printlnInt)
  )
)
