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
    (local $00_arr_ptr i32)
    (local $01_a i32)
    (local $02_i i32)
    ;; storing current offset value
    (local $offset i32)
    (global.get $offset)
    (local.set $offset)
    
    ;; reserving space for local array
    (global.get $offset)
    (local.tee $00_arr_ptr)
    (i32.const 40)
    (global.set $offset)
    
    
    (block $BLOCK_0
      (i32.const 0)
      (local.set $02_i)
      (local.get $02_i)
      (i32.const 9)
      (i32.lt_s)
      (i32.eqz)
      (br_if $BLOCK_0)
      (loop $FOR_0
        (local.get $00_arr_ptr)
        (local.set $01_a)
        (local.get $02_i)
        (i32.const 1)
        (i32.add)
        (local.set $02_i)
        (local.get $02_i)
        (i32.const 9)
        (i32.lt_s)
        (br_if $FOR_0)
      )
    )
    ;; restoring offset value
    (local.get $offset)
    (global.set $offset)
  )
)
