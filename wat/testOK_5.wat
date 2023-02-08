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
  (data(i32.const 0) "world")
  (data(i32.const 5) "hello")
  (data(i32.const 10) "!")
  (global $offset (mut i32) (i32.const 11))
  
  (func $getWorld (export "getWorld") (result i32)    
    
    (i32.const 0)
    (return)
  )
  
  (func $main (export "main")     
    (local $00_a i32)
    (local $01_b i32)
    (local $02_c f32)
    (local $03_d i32)
    
    
    
    
    
    (i32.const 1)
    (local.set $00_a)
    
    (i32.const 5)
    (call $getWorld)
    (i32.add)
    (i32.const 10)
    (i32.add)
    (local.set $01_b)
    
    (f32.const 1.0)
    (local.set $02_c)
    
    (i32.const 1)
    (local.set $03_d)
    
    (local.get $00_a)
    (call $printlnInt)
    (local.get $01_b)
    (i32.const 5)
    (call $printlnString)
    (local.get $02_c)
    (call $printlnFloat)
    (local.get $03_d)
    (call $printlnBoolean)
  )
)
