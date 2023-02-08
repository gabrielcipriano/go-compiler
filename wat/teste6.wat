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
  
  (func $__getArrPos (param $idx i32) (param $addr i32) (result i32)    
    (local.get $idx)
    (i32.const 4)
    (i32.mul)
    (local.get $addr)
    (i32.add)
  )
  ;; loads an i32 array val from memory
  (func $__loadArrValInt (param $idx i32) (param $addr i32) (result i32)    
    (local.get $idx)
    (local.get $addr)
    (call $__getArrPos)
    (i32.load)
  )
  
  ;; loads an f32 array val from memory
  (func $__loadArrValFloat (param $idx i32) (param $addr i32) (result f32)    
    (local.get $idx)
    (local.get $addr)
    (call $__getArrPos)
    (f32.load)
  )
  
  ;; adding strings to memory
  (global $offset (mut i32) (i32.const 0))
  
  (func $main (export "main")     
    (local $00_numeros_ptr i32)
    (local $01_i i32)
    (local $02_deTrasPraFrente_ptr i32)
    (local $03_i i32)
    (local $04_i i32)
    ;; storing current offset value
    (local $offset i32)
    (global.get $offset)
    (local.set $offset)
    
    ;; reserving space for local array
    (global.get $offset)
    (local.tee $00_numeros_ptr)
    (i32.const 20)
    (i32.add)
    (global.set $offset)
    
    (i32.const 10)
    ;; store array val in memory
    (global.set $aux_i32)
    (i32.const 0)
    (i32.const 4)
    (i32.mul)
    (local.get $00_numeros_ptr)
    (i32.add)
    (global.get $aux_i32)
    (i32.store)
    
    (i32.const 200)
    ;; store array val in memory
    (global.set $aux_i32)
    (i32.const 1)
    (i32.const 4)
    (i32.mul)
    (local.get $00_numeros_ptr)
    (i32.add)
    (global.get $aux_i32)
    (i32.store)
    
    (i32.const 3000)
    ;; store array val in memory
    (global.set $aux_i32)
    (i32.const 2)
    (i32.const 4)
    (i32.mul)
    (local.get $00_numeros_ptr)
    (i32.add)
    (global.get $aux_i32)
    (i32.store)
    
    (i32.const 40000)
    ;; store array val in memory
    (global.set $aux_i32)
    (i32.const 3)
    (i32.const 4)
    (i32.mul)
    (local.get $00_numeros_ptr)
    (i32.add)
    (global.get $aux_i32)
    (i32.store)
    
    (i32.const 500000)
    ;; store array val in memory
    (global.set $aux_i32)
    (i32.const 4)
    (i32.const 4)
    (i32.mul)
    (local.get $00_numeros_ptr)
    (i32.add)
    (global.get $aux_i32)
    (i32.store)
    
    (block $BLOCK_0
      (i32.const 0)
      (local.set $01_i)
      (local.get $01_i)
      (i32.const 5)
      (i32.lt_s)
      (i32.eqz)
      (br_if $BLOCK_0)
      (loop $FOR_0
        ;; loads a array val from memory
        (local.get $01_i)
        (local.get $00_numeros_ptr)
        (call $__loadArrValInt)
        (call $printlnInt)
        (local.get $01_i)
        (i32.const 1)
        (i32.add)
        (local.set $01_i)
        (local.get $01_i)
        (i32.const 5)
        (i32.lt_s)
        (br_if $FOR_0)
      )
    )
    
    ;; reserving space for local array
    (global.get $offset)
    (local.tee $02_deTrasPraFrente_ptr)
    (i32.const 20)
    (i32.add)
    (global.set $offset)
    
    (block $BLOCK_1
      (i32.const 4)
      (local.set $03_i)
      (local.get $03_i)
      (i32.const 0)
      (i32.ge_s)
      (i32.eqz)
      (br_if $BLOCK_1)
      (loop $FOR_1
        ;; loads a array val from memory
        (i32.const 4)
        (local.get $03_i)
        (i32.sub)
        (local.get $00_numeros_ptr)
        (call $__loadArrValInt)
        ;; store array val in memory
        (global.set $aux_i32)
        (local.get $03_i)
        (i32.const 4)
        (i32.mul)
        (local.get $02_deTrasPraFrente_ptr)
        (i32.add)
        (global.get $aux_i32)
        (i32.store)
        (local.get $03_i)
        (i32.const 1)
        (i32.sub)
        (local.set $03_i)
        (local.get $03_i)
        (i32.const 0)
        (i32.ge_s)
        (br_if $FOR_1)
      )
    )
    
    (block $BLOCK_2
      (i32.const 0)
      (local.set $04_i)
      (local.get $04_i)
      (i32.const 5)
      (i32.lt_s)
      (i32.eqz)
      (br_if $BLOCK_2)
      (loop $FOR_2
        ;; loads a array val from memory
        (local.get $04_i)
        (local.get $02_deTrasPraFrente_ptr)
        (call $__loadArrValInt)
        (call $printlnInt)
        (local.get $04_i)
        (i32.const 1)
        (i32.add)
        (local.set $04_i)
        (local.get $04_i)
        (i32.const 5)
        (i32.lt_s)
        (br_if $FOR_2)
      )
    )
    ;; restoring offset value
    (local.get $offset)
    (global.set $offset)
  )
)