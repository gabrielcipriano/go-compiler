#!/bin/bash
ROOT=/Users/cipriano/git/go-compiler
ANTLR_PATH=$ROOT/tools/antlr-4.11.1-complete.jar
CLASS_PATH_OPTION="-cp .:$ANTLR_PATH"
GRAMMAR_NAME=GO
GEN_PATH=$ROOT/src
GEN_PACKAGE=parser
DATA=$ROOT/tests
IN=$DATA/in_OK
OUT=$ROOT/wat
cd $GEN_PATH
infile=$1 # ../tests/in_OK/test_wasm_1.go
# for infile in `ls $IN/*.go`; do
  base=$(basename $infile)
  outfile=$OUT/${base/.go/.wat}
  echo Running $base

  # Make command and its output

  # If file exists, override it
  if [ -f $outfile ]; then
    echo
  else
    # If outfile doesn't exist, create it and write output of make command
    touch $outfile
  fi
  make run FILE="$infile" | sed '1d' > $outfile

# done
