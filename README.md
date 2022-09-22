# ppc_a1 Objectives

In this assignment we will tackle a common challenge in bioinformatics: detect and count number of DNA patterns in large DNA sequences.

A DNA sequence can be seen as an array of characters, in which only 'a', 't', 'c' and 'g' are valid. A DNA pattern is also a sequence, much smaller in size.

A practical example: Having the (uncommonly small, but useful for a demo) sequence "cgttttt" DNA sequence. If we are looking for the patterns "cgt" and "ttt", we will have the following matches:

"cgt" found at index 0
"ttt" found at index 2
"ttt" found at index 3
"ttt" found at index 4
Because we are looking for the number of matches, the actual answer should be [1,3] in the order in which the patterns appear in the original list.


Grade 17,5/20