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
    
    
    
    (i32.const 1)
    (local.set $00_a)
    
    (i32.const 2)
    (local.set $01_b)
    
    (local.get $00_a)
    (i32.const 1)
    (i32.eq)
    (if
      (then
        (block $BLOCK_0
          (i32.const 1)
          (local.set $00_a)
          (local.get $00_a)
          (local.get $01_b)
          (i32.le_s)
          (i32.eqz)
          (br_if $BLOCK_0)
          (loop $FOR_0
            (i32.const 0)
            (local.set $00_a)
            (local.get $00_a)
            (i32.const 1)
            (i32.add)
            (local.set $00_a)
            (local.get $00_a)
            (local.get $01_b)
            (i32.le_s)
            (br_if $FOR_0)
          )
        )
      )
    )
  )
)
