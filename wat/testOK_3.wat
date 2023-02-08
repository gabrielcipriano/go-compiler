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
  (data(i32.const 0) "verdadeiro")
  (data(i32.const 10) "falso")
  (data(i32.const 15) "1 maior que 0")
  (global $offset (mut i32) (i32.const 28))
  
  (func $doNothing (export "doNothing")     
    
  )
  
  (func $main (export "main")     
    
    (i32.const 1)
    (if
      (then
        (i32.const 0)
        (i32.const 10)
        (call $printlnString)
      )
    )
    
    (i32.const 0)
    (if
      (then
        (call $doNothing)
      )
      (else
        (i32.const 10)
        (i32.const 5)
        (call $printlnString)
      )
    )
    
    (i32.const 0)
    (if
      (then
      )
      (else
        (i32.const 1)
        (i32.const 0)
        (i32.gt_s)
        (if
          (then
            (i32.const 15)
            (i32.const 13)
            (call $printlnString)
          )
          (else
          )
        )
      )
    )
  )
)
