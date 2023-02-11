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
for infile in `ls $IN/*.go`; do
  base=$(basename $infile)
  outfile=$OUT/${base/.go/.wat}
  echo Running $base

  # Make command and its output
  make_output=$(make run FILE="$infile" | sed '1d')

  # If file exists, override it
  if [ -f $outfile ]; then
    echo "$make_output" > $outfile
  else
    # If outfile doesn't exist, create it and write output of make command
    touch $outfile
    echo "$make_output" > $outfile
  fi
done
