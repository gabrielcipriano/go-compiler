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
  (data(i32.const 0) "Hello")
  (global $offset (mut i32) (i32.const 5))
  
  (func $foo (export "foo") (param $00_x i32) (result i32)    
    
    (local.get $00_x)
    (return)
  )
  
  (func $main (export "main")     
    (local $01_num i32)
    (local $02_str i32)
    (local $03_floatin f32)
    
    (i32.const 1)
    (local.set $01_num)
    
    (i32.const 0)
    (local.set $02_str)
    
    (f32.const 1.0)
    (local.set $03_floatin)
  )
)
