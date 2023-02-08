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
  (global $aux_f32 (mut f32) (f32.const 0.000000))
  
  ;; adding strings to memory
  (data(i32.const 0) "\06\00\00\00")
  (data(i32.const 4) "fim :)")
  (global $offset (mut i32) (i32.const 10))
  
  (func $main (export "main")     
    (local $00_i i32)
    
    (block $BLOCK_0
      (i32.const 0)
      (local.set $00_i)
      (local.get $00_i)
      (i32.const 10)
      (i32.lt_s)
      (i32.eqz)
      (br_if $BLOCK_0)
      (loop $FOR_0
        (local.get $00_i)
        (call $printlnInt)
        (local.get $00_i)
        (i32.const 1)
        (i32.add)
        (local.set $00_i)
        (local.get $00_i)
        (i32.const 10)
        (i32.lt_s)
        (br_if $FOR_0)
      )
    )
    
    (i32.const 0)
    ;; prints string geting the size & moving offset
    (global.set $aux_i32)
    (global.get $aux_i32)
    (i32.const 4)
    (i32.add)
    (global.get $aux_i32)
    (i32.load)
    (call $printlnString)
  )
)