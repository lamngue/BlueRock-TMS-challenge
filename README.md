# BlueRock-TMS-challenge

## Optimization:
### The first 3 inputs are pretty quick, the other are slow
### Sorted by total number of non-empty cell ("X") in a piece, decreasing
### Added pruning, excluded memoization since the search space is too big (m*n^k) (k is the depth)
- Input 03.txt: takes less than 0.09 minutes
- Input 04.txt: takes 0.7 minutes
- Input 05.txt: takes 7 minutes
- Input 06.txt...: takes pretty long (unknown)
