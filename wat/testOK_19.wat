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
    (local $00_minhaArray_ptr i32)
    ;; storing current offset value
    (local $offset i32)
    (global.get $offset)
    (local.set $offset)
    
    ;; reserving space for local array
    (global.get $offset)
    (local.tee $00_minhaArray_ptr)
    (i32.const 40)
    (global.set $offset)
    
    (i32.const 5)
    (local.set $00_minhaArray_ptr)
    
    (local.get $00_minhaArray_ptr)
    (call $printlnInt)
    
    (local.get $00_minhaArray_ptr)
    (local.set $00_minhaArray_ptr)
    ;; restoring offset value
    (local.get $offset)
    (global.set $offset)
  )
)
