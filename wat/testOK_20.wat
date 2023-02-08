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
  
  (global $00_a (mut i32) (i32.const 2))
  
  (global $01_b (mut i32) (i32.const 3))
  
  (global $02_d (mut i32) (i32.const 0))
  
  (global $03_c (mut i32) (i32.const 0))
  
  (func $countdown (export "countdown") (param $04_val i32) (result i32)    
    
    (local.get $04_val)
    (i32.const 0)
    (i32.lt_s)
    (if
      (then
        (i32.const 0)
        (return)
      )
    )
    
    (local.get $04_val)
    (call $printlnInt)
    
    (local.get $04_val)
    (i32.const 1)
    (i32.sub)
    (call $countdown)
    (return)
  )
  
  (func $main (export "main") (result i32)    
    (local $05_rocketLaunch i32)
    
    (i32.const 10)
    (call $countdown)
    (local.set $05_rocketLaunch)
    
    (local.get $05_rocketLaunch)
    (return)
  )
)
