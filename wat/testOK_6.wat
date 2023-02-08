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
  
  (func $main (export "main")     
    (local $00_a i32)
    (local $01_b i32)
    (local $02_test i32)
    
    (i32.const 1)
    (i32.const 3)
    (i32.add)
    (i32.const 2)
    (i32.sub)
    (i32.const 13)
    (i32.const 4)
    (i32.div_s)
    (i32.add)
    (local.set $00_a)
    
    
    (i32.const 3)
    (i32.const 1)
    (i32.add)
    (i32.const 0)
    (i32.const 1)
    (i32.sub)
    (i32.const 2)
    (i32.mul)
    (i32.add)
    (i32.const 3)
    (i32.mul)
    (local.set $01_b)
    
    (local.get $01_b)
    (local.get $00_a)
    (i32.sub)
    (local.set $01_b)
    
    (local.get $00_a)
    (i32.const 3)
    (i32.const 5)
    (i32.mul)
    (i32.const 2)
    (i32.div_s)
    (i32.const 1)
    (i32.add)
    (i32.ge_s)
    (local.get $01_b)
    (i32.const 13)
    (i32.const 4)
    (i32.div_s)
    (i32.lt_s)
    (i32.or)
    (local.set $02_test)
  )
)
