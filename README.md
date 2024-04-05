# BlueRock-TMS-challenge

## Output format:
- Piece "k" placed at: (x, y)
- Since the pieces are sorted, the order of the output corresponds to the piece's initial index
- At the end of each input, print out the file name and execution time
- 
## Optimization:
#### The first 5 inputs are pretty quick, the other are slow
#### Sorted by total number of non-empty cell ("X") in a piece, decreasing
#### Added boundary checking for pieces
#### Added bit manipulation instead of regular math when updating the cell values
#### Added pruning, excluded memoization since the search space is too big (m*n^k) (k is the depth), unlikely to revisit the same state
- Input 03.txt: takes 0.014 minutes
- Input 04.txt: takes 0.37 minutes
- Input 05.txt: takes 0.03 minutes (wat?...)
- Input 06.txt: takes 92 minutes
- Input 07.txt:
- Input 08.txt:
- Input 09.txt: