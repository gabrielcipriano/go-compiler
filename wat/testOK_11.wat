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
    (local $01_b f32)
    (local $02_c f32)
    
    (i32.const 1)
    (local.set $00_a)
    
    (f32.const 2.0)
    (local.set $01_b)
    
    (local.get $00_a)
    (f32.convert_i32_s)
    (local.get $01_b)
    (f32.add)
    (local.set $02_c)
    
    (local.get $02_c)
    (call $printlnFloat)
  )
)
