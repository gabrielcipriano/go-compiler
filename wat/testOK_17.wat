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
  
  (func $soma (export "soma") (param $00_a i32) (param $01_b f32) (result f32)    
    (local $02_c f32)
    
    (local.get $00_a)
    (f32.convert_i32_s)
    (f32.const 3.5)
    (f32.add)
    (local.set $02_c)
    
    (local.get $02_c)
    (return)
  )
  
  (func $main (export "main")     
    (local $03_b f32)
    (local $04_c f32)
    (local $05_d f32)
    (local $06_f i32)
    (local $07_g i32)
    
    (i32.const 3)
    (f32.convert_i32_s)
    (local.set $03_b)
    
    (i32.const 4)
    (f32.convert_i32_s)
    (local.set $04_c)
    
    (i32.const 5)
    (f32.convert_i32_s)
    (local.set $05_d)
    
    
  )
)
