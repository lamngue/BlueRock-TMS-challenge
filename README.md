# BlueRock-TMS-challenge

## Optimization:
### The first 5 inputs are pretty quick, the other are slow
### Sorted by total number of non-empty cell ("X") in a piece, decreasing
### Added boundary checking for pieces
### Added pruning, excluded memoization since the search space is too big (m*n^k) (k is the depth), unlikely to revisit the same state
- Input 03.txt: takes 0.02 minutes
- Input 04.txt: takes 0.2 minutes
- Input 05.txt: takes 2 minutes
- Input 06.txt: takes 95 minutes
- Input 07.txt:
- Input 08.txt:
- Input 09.txt: