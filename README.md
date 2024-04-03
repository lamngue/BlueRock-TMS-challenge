# BlueRock-TMS-challenge

## Optimization:
### The first 3 inputs are pretty quick, the other are really slow
### Sorted by total number of non-empty cell ("X") in a piece, decreasing
- Input 03.txt: takes less than 1 minutes
- Input 04.txt: takes ~1 minutes
- Input 05.txt: takes 7-8 minutes
- Input 06.txt...: takes pretty long (unknown)

### Added Memoization and pruning, with LRU cache eviction policy (isn't much faster :(()
- Input 03.txt: takes less than 1 minutes
- Input 04.txt: takes ~1 minutes
- Input 05.txt: takes 10 minutes (interesting)
- Input 06.txt...: takes pretty long (unknown)