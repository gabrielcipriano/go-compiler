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
  (data(i32.const 0) "gabriel")
  (data(i32.const 7) "rogerio")
  (global $offset (mut i32) (i32.const 14))
  
  (global $00_nome (mut i32) (i32.const 0))
  
  (func $main (export "main")     
    (local $01_gerim i32)
    
    (i32.const 7)
    (local.set $01_gerim)
    
    (local.get $01_gerim)
    (i32.const 7)
    (call $printlnString)
  )
)
